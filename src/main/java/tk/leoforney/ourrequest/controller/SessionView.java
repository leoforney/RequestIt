package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;
import tk.leoforney.ourrequest.vaadin.MainLayout;

import static tk.leoforney.ourrequest.Application.appContext;

@PageTitle("OurRequest | Sessions")
@Route(value = "sessions", layout = MainLayout.class)
public class SessionView extends Div implements RouterLayout {

    private CustomUserDetailsService userService;
    private SpotifyAuthRepository spotifyAuthRepository;
    private PartySessionRepository partySessionRepository;

    public SessionView() {
        userService = (CustomUserDetailsService) appContext.getAutowireCapableBeanFactory().getBean("customUserDetailsService");
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");


        add(new SessionsMainView(userService, spotifyAuthRepository, partySessionRepository));

    }
}
