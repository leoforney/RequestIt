package tk.leoforney.requestit.client.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kong.unirest.Unirest;
import tk.leoforney.requestit.client.Application;
import tk.leoforney.requestit.client.model.TrackWrapper;
import tk.leoforney.requestit.client.service.AuthenticationService;
import tk.leoforney.requestit.client.service.WebsocketService;
import tk.leoforney.requestit.spotify.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainController {

    @FXML
    VBox buttonBox;

    private ObservableList<TrackWrapper> trackList = FXCollections.observableArrayList();
    private ObservableList<Image> trackArtList = FXCollections.observableArrayList();

    private WebsocketService websocketService;
    private AuthenticationService authService;

    private Gson gson;

    public MainController() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @FXML
    protected void initialize() {
        websocketService = (WebsocketService) Application.getSpringContext().getAutowireCapableBeanFactory().getBean("websocketService");
        authService = (AuthenticationService) Application.getSpringContext().getAutowireCapableBeanFactory().getBean("authenticationService");

        if (authService.dataPersisted() && authService.authenticated()) {
            String responseString = Unirest.get("http://localhost:8080/rest/session/getRequestedTracks")
                    .basicAuth(authService.getEmail(), authService.getPassword())
                    .header("sessionId", "leog19")
                    .asString().getBody();

            System.out.println(responseString);

            List<Track> decodedList = gson.fromJson(responseString, new TypeToken<ArrayList<Track>>(){}.getType());

            for (Track track: decodedList) {
                trackList.add(new TrackWrapper(track));
            }




            JFXListView<TrackWrapper> birdList = new JFXListView<>();
            birdList.setItems(trackList);
            birdList.setCellFactory(param -> new SongCell());
            birdList.setPrefWidth(180);

            VBox layout = new VBox(birdList);
            layout.setPadding(new Insets(10));

            buttonBox.getChildren().add(layout);

        }


    }

    private static final String PREFIX =
            "http://icons.iconarchive.com/icons/jozef89/origami-birds/72/bird";

    private static final String SUFFIX =
            "-icon.png";

    private static final ObservableList<String> birds = FXCollections.observableArrayList(
            "-black",
            "-blue",
            "-red",
            "-red-2",
            "-yellow",
            "s-green",
            "s-green-2"
    );

    private static final ObservableList<Image> birdImages = FXCollections.observableArrayList();

    private class SongCell extends JFXListCell<TrackWrapper> {
        private HBox hBox = new HBox();
        private VBox trackInformationVBox = new VBox();
        private ImageView imageView = new ImageView();
        private Label trackName = new Label();
        private Label trackAlbumName = new Label();
        private Label trackArtist = new Label();
        private String trackId;

        public SongCell() {
            JFXListCell thisCell = this;

            hBox.setPadding(new Insets(0l, 10, 10, 0));
            trackName.setPadding(new Insets(0, 10, 0, 10));
            trackAlbumName.setPadding(new Insets(0, 10, 0, 10));
            trackArtist.setPadding(new Insets(0, 10, 0, 10));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setAlignment(Pos.CENTER);

            setOnDragDetected(event -> {
                if (getItem() == null) {
                    return;
                }

                ObservableList<TrackWrapper> items = getListView().getItems();

                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(getItem().getTrack().getId());
                dragboard.setDragView(
                        getItem().getImage()
                );
                dragboard.setContent(content);

                event.consume();
            });

            setOnDragOver(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });

            setOnDragEntered(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(0.3);
                }
            });

            setOnDragExited(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(1);
                }
            });

            setOnDragDropped(event -> {
                if (getItem() == null) {
                    return;
                }

                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    ObservableList<TrackWrapper> items = getListView().getItems();
                    int draggedIdx = 0;
                    for (TrackWrapper track: trackList) {
                        if (track.getTrack().getId().equals(db.getString())) {
                            draggedIdx = trackList.indexOf(track);
                        }
                    }
                    int thisIdx = items.indexOf(getItem());

                    Image temp = birdImages.get(draggedIdx);
                    birdImages.set(draggedIdx, birdImages.get(thisIdx));
                    birdImages.set(thisIdx, temp);

                    items.set(draggedIdx, getItem());
                    for (TrackWrapper track: trackList) {
                        if (track.getTrack().getId().equals(db.getString())) {
                            items.set(thisIdx, track);
                        }
                    }

                    List<TrackWrapper> itemscopy = new ArrayList<>(getListView().getItems());
                    getListView().getItems().setAll(itemscopy);

                    success = true;
                }
                event.setDropCompleted(success);

                event.consume();
            });

            setOnDragDone(DragEvent::consume);
        }

        @Override
        protected void updateItem(TrackWrapper item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                trackId = item.getTrack().getId();
                if (item.getImage() != null) {
                    imageView.setImage(item.getImage());
                }
                trackName.setText(item.getTrack().getName());
                trackAlbumName.setText(item.getTrack().getAlbum().getName());
                trackArtist.setText(item.getTrack().getArtist().getName());
                if (!trackName.getText().equals(trackAlbumName.getText())) {
                    trackInformationVBox = new VBox(trackName, trackAlbumName, trackArtist);
                } else {
                    trackInformationVBox = new VBox(trackName, trackArtist);
                }

                hBox = new HBox(imageView, trackInformationVBox);
                setGraphic(hBox);
            }
        }
    }

}