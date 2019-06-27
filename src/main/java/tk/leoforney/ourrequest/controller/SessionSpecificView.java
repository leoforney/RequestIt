package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.communication.PushMode;
import tk.leoforney.ourrequest.listener.RequestListener;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.spotify.Track;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;
import tk.leoforney.ourrequest.vaadin.MainLayout;

import java.util.List;

import static tk.leoforney.ourrequest.Application.appContext;

@Route(value = "session", layout = MainLayout.class)
public class SessionSpecificView extends VerticalLayout implements RequestListener, HasUrlParameter<String>, HasDynamicTitle {

    private H3 newestAddedSong;
    private ListBox<Track> trackListBox;
    CustomUserDetailsService userService;
    SpotifyAuthRepository spotifyAuthRepository;
    PartySessionRepository partySessionRepository;
    String sessionId;
    String title;

    public SessionSpecificView() {

        userService = (CustomUserDetailsService) appContext.getAutowireCapableBeanFactory().getBean("customUserDetailsService");
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");

    }


    List<Track> refreshedRequestedTracks() {
        return partySessionRepository.findBy_id(sessionId).getRequestedTracks();
    }

    List<Track> refreshedAcceptedTracks() {
        return partySessionRepository.findBy_id(sessionId).getAcceptedTracks();
    }

    PartySession session;

    @Override
    public void setParameter(BeforeEvent beforeEvent, String sessionId) {
        this.sessionId = sessionId;
        session = partySessionRepository.findBy_id(sessionId);

        session.getNotifier().addListener(this);

        add(new H1("Session: " + session.getSessionName()));
        title = "OurRequest | " + session.getSessionName();
        newestAddedSong = new H3("Newest added song: ");
        add(newestAddedSong);

        trackListBox = new ListBox<>();

        trackListBox.setItems(refreshedRequestedTracks());

        trackListBox.setRenderer(new ComponentRenderer<>(item -> {
            Label name = new Label(item.getName());
            Label stock = new Label(item.getArtist().getName());

            Button playButton = new Button(VaadinIcon.PLAY.create(), event -> {
                //item.setStock(item.getStock() - 1);
                //listBox.getDataProvider().refreshItem(item);
                // TODO: Implement song sample, accept and decline buttons
            });

            Button acceptButton = new Button("Accept", event -> {
                acceptTrack(item);
            });

            Button declineButton = new Button("Decline", event -> {
                declineTrack(item);
            });

            Div labels = new Div(name, stock);
            Div layout = new Div(labels, playButton, acceptButton, declineButton);

            labels.getStyle().set("display", "flex")
                    .set("flexDirection", "column").set("marginRight", "0px");
            layout.getStyle().set("display", "flex")
                    .set("alignItems", "center");

            return layout;
        }));

        add(trackListBox);
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    public void acceptTrack(Track track) {
        session.acceptTrack(track);
        partySessionRepository.save(session);

        trackListBox.setItems(refreshedRequestedTracks());
        this.getUI().ifPresent(ui -> ui.access((Command) () -> {
            if (!ui.getPushConfiguration().getPushMode().equals(PushMode.DISABLED)) {
                ui.push();
            }
        }));
    }

    public void declineTrack(Track track) {
        session.deleteRequestedTrack(track);
        partySessionRepository.save(session);

        trackListBox.setItems(refreshedRequestedTracks());
        this.getUI().ifPresent(ui -> ui.access((Command) () -> {
            if (!ui.getPushConfiguration().getPushMode().equals(PushMode.DISABLED)) {
                ui.push();
            }
        }));

    }

    @Override
    public void trackRequested(Track addedTrack) {
        this.getUI().ifPresent(ui -> ui.access((Command) () -> {
            newestAddedSong.setText("Newest added song: " + addedTrack.getName());
            Notification notification = new Notification("Added song: " + addedTrack.getName());
            notification.setDuration(5000);

            Label label = new Label("New song added " + addedTrack.getName() + " - " + addedTrack.getArtist().getName());

            Button acceptButton = new Button("Accept");
            acceptButton.setId("acceptButtonNotification");
            acceptButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
                acceptTrack(addedTrack);
                notification.close();
            });

            Button declineButton = new Button("Decline");
            acceptButton.setId("declineButtonNotification");
            declineButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
                declineTrack(addedTrack);
                notification.close();
            });

            notification.add(new VerticalLayout(label, new HorizontalLayout(acceptButton, declineButton)));

            notification.open();

            trackListBox.setItems(refreshedRequestedTracks());
            if (!ui.getPushConfiguration().getPushMode().equals(PushMode.DISABLED)) {
                ui.push();
            }
        }));
    }

    @Override
    public void trackAccepted(Track track) {
        Notification.show("Track accepted");
    }
}
