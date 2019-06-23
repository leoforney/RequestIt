package tk.leoforney.ourrequest.controller.player;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import org.vaadin.zhe.PaperRangeSlider;
import tk.leoforney.ourrequest.controller.player.model.SpotifyWebState;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.model.spotify.Track;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.rest.SearchController;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;
import tk.leoforney.ourrequest.vaadin.MainLayout;

import java.util.ArrayList;
import java.util.List;

import static tk.leoforney.ourrequest.Application.appContext;

@Route(value = "play", layout = MainLayout.class)
public class PlayerView extends VerticalLayout implements PlayerComponent.StateChangeCallback, RouterLayout, HasUrlParameter<String>, HasDynamicTitle, ComponentEventListener<PaperRangeSlider.MaxValueChangeEvent> {

    CustomUserDetailsService userService;
    SpotifyAuthRepository spotifyAuthRepository;
    PartySessionRepository partySessionRepository;
    SearchController searchController;

    public PlayerView() {

        userService = (CustomUserDetailsService) appContext.getAutowireCapableBeanFactory().getBean("customUserDetailsService");
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");
        searchController = (SearchController) appContext.getAutowireCapableBeanFactory().getBean("searchController");

        add(new H2("Player"));

    }

    private String sessionId;
    private SpotifyAuthorization authorization;
    private PlayerComponent component;
    private PartySession session;
    private Button playPauseButton;
    private boolean firstPlayPush = false;

    @Override
    public void setParameter(BeforeEvent event, String sessionId) {
        this.sessionId = sessionId;
        session = partySessionRepository.findBy_id(sessionId);

        if (session.isValid()) {
            authorization = spotifyAuthRepository.findByEmail(session.getEmail());
            if (!authorization.isValid()) {
                authorization = userService.refreshAccessToken(authorization.getEmail());
            }

            component = new PlayerComponent(this, authorization);
            component.addCallback(this);

            playPauseButton = new Button(VaadinIcon.PLAY.create());
            playPauseButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> event) {
                    if (allTrackList.size() != 0) {
                        if (!firstPlayPush) {
                            component.playSong(session.getRequestedTracks().get(0));
                            firstPlayPush = true;
                        } else {
                            component.togglePlayback();
                        }
                    }
                }
            });

            Button nextButton = new Button(VaadinIcon.FORWARD.create());
            nextButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) event12 -> component.next());

            Button lastButton = new Button(VaadinIcon.BACKWARDS.create());
            lastButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) event1 -> component.previous());

            PaperRangeSlider paperRangeSlider = new PaperRangeSlider();
            add(paperRangeSlider);
            paperRangeSlider.setSingleSlider(true);
            paperRangeSlider.setMin(0);
            paperRangeSlider.setMax(100);

            paperRangeSlider.addMaxValueChangeListener(this);

            ComboBox<PlaylistSimplified> selectPlaylist = new ComboBox<>();
            selectPlaylist.setItems(searchController.getPlaylists(authorization));
            selectPlaylist.setItemLabelGenerator(new ItemLabelGenerator<PlaylistSimplified>() {
                @Override
                public String apply(PlaylistSimplified item) {
                    return item.getName();
                }
            });
            selectPlaylist.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<PlaylistSimplified>, PlaylistSimplified>>() {
                @Override
                public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<PlaylistSimplified>, PlaylistSimplified> event) {
                    allTrackList = new ArrayList<>();
                    allTrackList.addAll(session.getRequestedTracks());
                    allTrackList.addAll(searchController.getTracksInPlaylist(authorization, event.getValue()));
                }
            });
            selectPlaylist.setLabel("Select playlist as base");
            add(selectPlaylist);

            add(new HorizontalLayout(lastButton, playPauseButton, nextButton, new Label("Volume: "), paperRangeSlider));
            add(component);

        }

    }
    private List<Track> allTrackList;

    private String title = "OurRequest | Play";

    @Override
    public String getPageTitle() {
        return title;
    }

    @Override
    public void onComponentEvent(PaperRangeSlider.MaxValueChangeEvent event) {
        component.setVolume(event.getValueMax()/100);
    }

    int currentSong = 0;
    long lastTimestamp = 0;

    @Override
    public void stateChanged(SpotifyWebState state) {
        if (state.getPosition() == 0 && state.getPaused()) { // Next song requested
            session = partySessionRepository.findBy_id(sessionId);
            if (allTrackList.size() > currentSong+1) {
                if (lastTimestamp+500 < System.currentTimeMillis()) {
                    currentSong++;
                    // Execute next song from playing queue
                    component.playSong(allTrackList.get(currentSong));
                }
                lastTimestamp = System.currentTimeMillis();
            } else {
                // Queue completed, show reset button
            }
        }
        if (!state.getPaused()) {
            playPauseButton.setIcon(VaadinIcon.PAUSE.create());
        } else {
            playPauseButton.setIcon(VaadinIcon.PLAY.create());
        }
    }
}
