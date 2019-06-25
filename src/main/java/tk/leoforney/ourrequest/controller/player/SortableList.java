package tk.leoforney.ourrequest.controller.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import tk.leoforney.ourrequest.model.spotify.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Tag("sortable-jquery")
@JavaScript("frontend://js/jquery-3.4.1.min.js")
@JavaScript("frontend://bower_components/Sortable/Sortable.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/jquery-sortablejs@latest/jquery-sortable.js")
@JavaScript("frontend://js/list.js")
@StyleSheet("frontend://styles/list.css")
public class SortableList extends Component implements HasComponents {

    private List<Track> requestedTracks, regularTracks, allTracks, tempList;
    private static ObjectMapper mapper;

    public SortableList() {
        setId("sortableList");
    }

    public void addItem(Component component) {
        add(component);
    }

    public void addTrackList(List<Track> requestedTracks, List<Track> regularTracks) {
        getElement().removeAllChildren();
        this.regularTracks = regularTracks;
        this.requestedTracks = requestedTracks;
        allTracks = new ArrayList<>();
        for (Track track : requestedTracks) {
            allTracks.add(track);
            add(new ListItem(track, true));
        }
        for (Track track : regularTracks) {
            allTracks.add(track);
            add(new ListItem(track, false));
        }
    }

    public List<Track> getCurrentTrackListOrder() {
        // Because of the lack of function to retrieve the data from the list, as objects
        // TODO: Deconstruct DOM and resolve into the list
        tempList = new ArrayList<>();
        /*
        getUI().ifPresent(ui -> ui.access((Command) () ->
                getElement().getChildren().forEach(element -> {
                    String id = element.getChild(0).getAttribute("id");
                    System.out.println(element.getChild(0).getOuterHTML());
                    for (Track track : allTracks) {
                        if (track.getId().equals(id)) {
                            tempList.add(track);
                            break;
                        }
                    }
                })));
*/
        getElement().executeJavaScript("retrieveOrder()");
        return tempList;
    }

    @ClientCallable
    public void updateListOrder(String jsonData) {
        if (mapper == null) mapper = new ObjectMapper();
        List<String> trackIds = null;
        try {
            trackIds = mapper.readValue(jsonData, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String id : trackIds) {
            for (Track track : allTracks) {
                if (track.getId().equals(id)) {
                    System.out.println(track.getName());
                    tempList.add(track);
                    break;
                }
            }
        }
    }


}
