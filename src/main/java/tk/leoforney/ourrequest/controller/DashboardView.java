package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tk.leoforney.ourrequest.model.User;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;

import static tk.leoforney.ourrequest.Application.appContext;

@Route("dashboard")
@PageTitle("OurRequest | Dashboard")
@Theme(value = Material.class, variant = Material.DARK)
public class DashboardView extends VerticalLayout {

    private CustomUserDetailsService userService;
    private SpotifyAuthRepository spotifyAuthRepository;
    private PartySessionRepository partySessionRepository;

    public DashboardView() {

        add(new H2("Dashboard"));

        userService = (CustomUserDetailsService) appContext.getAutowireCapableBeanFactory().getBean("customUserDetailsService");
        spotifyAuthRepository = (SpotifyAuthRepository) appContext.getAutowireCapableBeanFactory().getBean("spotifyAuthRepository");
        partySessionRepository = (PartySessionRepository) appContext.getAutowireCapableBeanFactory().getBean("partySessionRepository");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (user != null) {
            add(new Label("Welcome to your dashboard " + user.getFullname()));
            user.getRoles().forEach(role -> add(new Label("Role: " + role.getRole())));

            if (spotifyAuthRepository.findByEmail(user.getEmail()) == null) {
                Anchor anchor = new Anchor("https://accounts.spotify.com/authorize" +
                        "?client_id=6bef4555fccb4908a6f5fbafcb6604f1&" +
                        "response_type=code&" +
                        "redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fdashboard%2Fspotify&" +
                        "state=" + user.getId(), "Link Spotify");
                add(anchor);
            }
        }

        Button dashboard = new Button("Sessions");
        dashboard.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> dashboard.getUI().ifPresent(ui -> ui.navigate("sessions")));
        add(dashboard);

    }
}
