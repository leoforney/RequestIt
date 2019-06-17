package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;

import static tk.leoforney.ourrequest.Application.appContext;

@Route("session")
@PageTitle("OurRequest | Sessions")
@Theme(value = Material.class, variant = Material.DARK)
public class SessionView extends Div implements HasUrlParameter<String> {

    private String sessionId = "";

    private CustomUserDetailsService userService;
    private SpotifyAuthRepository spotifyAuthRepository;
    private PartySessionRepository partySessionRepository;

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {

        userService = (CustomUserDetailsService) appContext.getAutowireCapableBeanFactory().getBean("customUserDetailsService");
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");

        if (parameter != null) {
            sessionId = parameter;
            if (partySessionRepository.findBy_id(parameter) != null) { // Show page for all sessions
                removeAll();
                add(new SessionSpecificView(userService, spotifyAuthRepository, partySessionRepository, sessionId));
            }
        } else {
            if (sessionId.equals("")) {
                add(new SessionsMainView(userService, spotifyAuthRepository, partySessionRepository));
            }
        }
    }
}
