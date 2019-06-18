package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;
import tk.leoforney.ourrequest.vaadin.MainLayout;

import static tk.leoforney.ourrequest.Application.appContext;

@Route(value = "/play", layout = MainLayout.class)
public class PlayerView extends VerticalLayout implements RouterLayout, HasUrlParameter<String> {

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

    @Override
    public void setParameter(BeforeEvent event, String sessionId) {
        this.sessionId = sessionId;
        PartySession session = partySessionRepository.findBy_id(sessionId);

    }
}
