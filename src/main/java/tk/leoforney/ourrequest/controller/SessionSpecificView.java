package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.Command;
import tk.leoforney.ourrequest.listener.RequestListener;
import tk.leoforney.ourrequest.listener.RequestNotifier;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.spotify.Track;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;

public class SessionSpecificView extends VerticalLayout implements RequestListener {

    RequestNotifier notifier = RequestNotifier.getInstance();
    private H3 newestAddedSong;

    public SessionSpecificView(CustomUserDetailsService userService,
                               SpotifyAuthRepository spotifyAuthRepository,
                               PartySessionRepository partySessionRepository,
                               String sessionId) {

        notifier.addListener(this);

        PartySession session = partySessionRepository.findBy_id(sessionId);

        add(new H1("Session: " + session.getSessionName()));
        newestAddedSong = new H3();
        add(newestAddedSong);

        ListBox<Track> trackListBox = new ListBox<>();

        trackListBox.setItems(session.getRequestedTracks());

        trackListBox.setRenderer(new ComponentRenderer<>(item -> {
            Label name = new Label(item.getName());
            Label stock = new Label(item.getArtist().getName());

            Button button = new Button("Play", event -> {
                //item.setStock(item.getStock() - 1);
                //listBox.getDataProvider().refreshItem(item);
            });

            Div labels = new Div(name, stock);
            Div layout = new Div(labels, button);

            labels.getStyle().set("display", "flex")
                    .set("flexDirection", "column").set("marginRight", "0px");
            layout.getStyle().set("display", "flex")
                    .set("alignItems", "center");

            return layout;
        }));

        add(trackListBox);

    }

    @Override
    public void trackAdded(Track addedTrack) {
        this.getUI().ifPresent(ui -> ui.access(new Command() {
            @Override
            public void execute() {
                System.out.println("UI Present");
                newestAddedSong.setText("Newest added song: " + addedTrack.getName());
            }
        }));
        System.out.println("Track added");
    }

}
