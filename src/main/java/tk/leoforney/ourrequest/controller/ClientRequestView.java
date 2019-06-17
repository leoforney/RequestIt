package tk.leoforney.ourrequest.controller;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;
import com.wrapper.spotify.model_objects.specification.Track;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.rest.SearchController;
import tk.leoforney.ourrequest.service.QueueService;

import java.util.ArrayList;
import java.util.List;

import static tk.leoforney.ourrequest.Application.appContext;

@Route("r")
@PageTitle("OurRequest | Request")
@Theme(value = Material.class, variant = Lumo.LIGHT)
@HtmlImport("frontend://styles/lumo-styles.html")
public class ClientRequestView extends VerticalLayout implements HasUrlParameter<String>, ComponentEventListener {

    private SpotifyAuthRepository spotifyAuthRepository;
    private PartySessionRepository partySessionRepository;
    private SearchController searchController;
    private QueueService queueService;
    private PartySessionRepository partySessions;

    private PartySession session;
    private SpotifyAuthorization sa;

    private Autocomplete autocomplete;
    private H4 sessionTitle;
    private String selectedSong;
    private Label selectedSongLabel;

    public ClientRequestView() {
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");
        searchController = (SearchController) appContext.getAutowireCapableBeanFactory().getBean("searchController");
        queueService = (QueueService) appContext.getAutowireCapableBeanFactory().getBean("queueService");
        partySessions = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");

        autocomplete = new Autocomplete(5);

        autocomplete.addAutocompleteValueAppliedListener(event -> {
            selectedSongLabel.setText("Selection: " + event.getValue());
        });

        autocomplete.addValueClearListener(event -> {
            autocomplete.setOptions(new ArrayList<>());
        });

        autocomplete.setLabel("Search for song");
        autocomplete.setPlaceholder("Old Town Road");

        sessionTitle = new H4("Request song for ");

        add(sessionTitle, autocomplete);

        selectedSongLabel = new Label();
        add(selectedSongLabel);

        autocomplete.addChangeListener(this);
        autocomplete.addAutocompleteValueAppliedListener(this);

        Button requestButton = new Button("Request");
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
        if (event instanceof Autocomplete.ValueChangeEvent) {
            Autocomplete.ValueChangeEvent o = (Autocomplete.ValueChangeEvent) event;
            String text = o.getValue();
            if (!text.equals("")) {
                List<Track> songs = searchController.searchSongs(sa, text);

                List<String> songNames = new ArrayList<>();

                for (Track trackIterated: songs) {
                    songNames.add(trackIterated.getName() + " " + trackIterated.getArtists()[0].getName());
                }

                autocomplete.setOptions(songNames);
            }
        }
        if (event instanceof Autocomplete.AutocompleteValueAppliedEvent) {
            Autocomplete.AutocompleteValueAppliedEvent o = (Autocomplete.AutocompleteValueAppliedEvent) event;
            selectedSong = o.getValue();
        }
        if (event instanceof ClickEvent) {
            if (selectedSong != null && session != null) {
                boolean added = queueService.queueSong(session, selectedSong);
                if (added) {
                    Notification.show("Request for " + selectedSong + " put in.");
                } else {
                    Notification.show("This song is already requested!");
                }

            } else {
                Notification.show("You must select a song.");
            }
        }
    }
}
