package tk.leoforney.requestit.controller;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import tk.leoforney.requestit.repository.PartySessionRepository;
import tk.leoforney.requestit.repository.SpotifyAuthRepository;
import tk.leoforney.requestit.service.CustomUserDetailsService;
import tk.leoforney.requestit.vaadin.MainLayout;

import static tk.leoforney.requestit.Application.appContext;

@PageTitle("RequestIt | Sessions")
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
