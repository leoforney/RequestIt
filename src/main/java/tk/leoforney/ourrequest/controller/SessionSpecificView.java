package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import tk.leoforney.ourrequest.listener.RequestListener;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.spotify.Track;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;
import tk.leoforney.ourrequest.vaadin.MainLayout;

import static tk.leoforney.ourrequest.Application.appContext;

@Route(value = "session", layout = MainLayout.class)
public class SessionSpecificView extends VerticalLayout implements RequestListener, HasUrlParameter<String> {

    private H3 newestAddedSong;
    private ListBox<Track> trackListBox;
    CustomUserDetailsService userService;
    SpotifyAuthRepository spotifyAuthRepository;
    PartySessionRepository partySessionRepository;
    String sessionId;

    public SessionSpecificView() {

        userService = (CustomUserDetailsService) appContext.getAutowireCapableBeanFactory().getBean("customUserDetailsService");
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");

    }

    @Override
    public void trackAdded(Track addedTrack) {
        System.out.println("Track added");
        this.getUI().ifPresent(ui -> ui.access(new Command() {
            @Override
            public void execute() {
                System.out.println("Track added");
                newestAddedSong.setText("Newest added song: " + addedTrack.getName());
                Notification.show("Added song: " + addedTrack.getName());
                ui.push();
            }
        }));
        System.out.println("Track added");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String sessionId) {
        PartySession session = partySessionRepository.findBy_id(sessionId);

        session.getNotifier().addListener(this);

        add(new H1("Session: " + session.getSessionName()));
        newestAddedSong = new H3("Newest added song: ");
        add(newestAddedSong);

        trackListBox = new ListBox<>();

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
}
