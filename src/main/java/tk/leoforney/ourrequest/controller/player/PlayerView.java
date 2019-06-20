package tk.leoforney.ourrequest.controller.player;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.vaadin.zhe.PaperRangeSlider;
import tk.leoforney.ourrequest.controller.player.model.SpotifyWebState;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;
import tk.leoforney.ourrequest.vaadin.MainLayout;

import static tk.leoforney.ourrequest.Application.appContext;

@Route(value = "play", layout = MainLayout.class)
public class PlayerView extends VerticalLayout implements PlayerComponent.StateChangeCallback, RouterLayout, HasUrlParameter<String>, HasDynamicTitle, ComponentEventListener<PaperRangeSlider.MaxValueChangeEvent> {

    CustomUserDetailsService userService;
    SpotifyAuthRepository spotifyAuthRepository;
    PartySessionRepository partySessionRepository;

    public PlayerView() {

        userService = (CustomUserDetailsService) appContext.getAutowireCapableBeanFactory().getBean("customUserDetailsService");
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");

        add(new H2("Player"));

    }

    private String sessionId;
    private SpotifyAuthorization authorization;
    private PlayerComponent component;

    @Override
    public void setParameter(BeforeEvent event, String sessionId) {
        this.sessionId = sessionId;
        PartySession session = partySessionRepository.findBy_id(sessionId);

        authorization = spotifyAuthRepository.findByEmail(session.getEmail());
        if (!authorization.isValid()) {
            authorization = userService.refreshAccessToken(authorization.getEmail());
        }

        component = new PlayerComponent(this, authorization);
        component.addCallback(this);

        Button button = new Button("Play");
        button.addClickListener((ComponentEventListener<ClickEvent<Button>>) event1 -> component.playSong(session.getRequestedTracks().get(0)));

        Button pauseButton = new Button("Toggle Playback");
        pauseButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) event12 -> component.togglePlayback());

        PaperRangeSlider paperRangeSlider = new PaperRangeSlider();
        add(paperRangeSlider);
        paperRangeSlider.setSingleSlider(true);
        paperRangeSlider.setMin(0);
        paperRangeSlider.setMax(100);

        paperRangeSlider.addMaxValueChangeListener(this);

        add(button, pauseButton, paperRangeSlider);
        add(component);

    }

    private String title = "OurRequest | Play";

    @Override
    public String getPageTitle() {
        return title;
    }

    @Override
    public void onComponentEvent(PaperRangeSlider.MaxValueChangeEvent event) {
        component.setVolume(event.getValueMax()/100);
    }

    @Override
    public void stateChanged(SpotifyWebState state) {

    }
}
