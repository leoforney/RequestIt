package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import com.wrapper.spotify.model_objects.specification.Track;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.rest.SearchController;
import tk.leoforney.ourrequest.service.QueueService;

import java.util.List;

import static tk.leoforney.ourrequest.Application.appContext;

@Route(value = "r")
@Theme(value = Material.class, variant = Material.DARK)
@PageTitle("OurRequest | Request")
public class ClientRequestView extends VerticalLayout implements HasUrlParameter<String>, ComponentEventListener {

    private SpotifyAuthRepository spotifyAuthRepository;
    private PartySessionRepository partySessionRepository;
    private SearchController searchController;
    private QueueService queueService;
    private PartySessionRepository partySessions;

    private PartySession session;
    private SpotifyAuthorization sa;

    private H4 sessionTitle;
    private String selectedSong;
    private RadioButtonGroup<Track> radioButtons;
    private TextField songTextField;

    public ClientRequestView() {
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");
        searchController = (SearchController) appContext.getAutowireCapableBeanFactory().getBean("searchController");
        queueService = (QueueService) appContext.getAutowireCapableBeanFactory().getBean("queueService");
        partySessions = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");

        sessionTitle = new H4("Request song for ");

        songTextField = new TextField("Song name");

        Button searchButton = new Button("Search");
        searchButton.setId("searchButton");

        searchButton.addClickListener(this);

        radioButtons = new RadioButtonGroup<>();
        radioButtons.setRenderer(new TextRenderer<>(
                (ItemLabelGenerator<Track>) item -> item.getName() + " - " + item.getArtists()[0].getName()));

        add(sessionTitle, new HorizontalLayout(songTextField, searchButton), radioButtons);

        Button requestButton = new Button("Request");
        requestButton.setId("requestButton");
        requestButton.addClickListener(this);
        add(requestButton);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {

        session = partySessions.findBy_id(parameter);

        if (session == null) {
            removeAll();
            add(new H1("Session doesn't exist! Make sure you typed in the code correctly"));
        } else {
            sa = spotifyAuthRepository.findByEmail(session.getEmail());
            sessionTitle.setText("Request song for " + session.getSessionName());
        }

    }

    @Override
    public void onComponentEvent(ComponentEvent event) {
        if (event instanceof ClickEvent) {
            event.getSource().getId().ifPresent(s -> {
                if (s.equals("searchButton")) {
                    if (!songTextField.getValue().equals("")) {
                        List<Track> songs = searchController.searchSongs(sa, songTextField.getValue());

                        radioButtons.setItems(songs);
                    } else {
                        Notification.show("You must type in a song name");
                    }

                } else if (s.equals("requestButton")) {
                    if (radioButtons.getOptionalValue().isPresent() && session != null) {
                        boolean added = queueService.queueSong(session, radioButtons.getValue());
                        if (added) {
                            Notification.show("Request for " + radioButtons.getValue().getName() + " put in.");
                        } else {
                            Notification.show("This song is already requested!");
                        }

                    } else {
                        Notification.show("You must select a song.");
                    }
                }
            });
        }
    }
}
