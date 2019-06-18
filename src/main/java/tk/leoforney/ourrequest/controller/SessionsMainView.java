package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.User;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;

import java.util.List;

public class SessionsMainView extends VerticalLayout {

    public SessionsMainView(CustomUserDetailsService userService,
                            SpotifyAuthRepository spotifyAuthRepository,
                            PartySessionRepository partySessionRepository) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        add(new H1("Party Sessions"));


        if (user != null) {
            List<PartySession> sessionList = partySessionRepository.findAllByEmail(user.getEmail());

            Grid<PartySession> sessionGrid = new Grid<>(PartySession.class);
            sessionGrid.setItems(sessionList);

            sessionGrid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<PartySession>>() {
                @Override
                public void onComponentEvent(ItemDoubleClickEvent<PartySession> event) {
                    event.getSource().getUI().ifPresent(ui -> {
                        ui.navigate("session/" + event.getItem().getId());
                        ui.getPage().reload();
                    });
                }
            });

            sessionGrid.removeAllColumns();

            sessionGrid.addColumn("sessionName").setHeader("Name");
            sessionGrid.addColumn("id").setHeader("Session ID");
            sessionGrid.addColumn("expiration").setHeader("Expiration Date");
            add(sessionGrid);

            HorizontalLayout operationalButtons = new HorizontalLayout();

            Button newSession = new Button("Create new session");

            newSession.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                NewSessionDialog dialog = new NewSessionDialog(userService, spotifyAuthRepository, partySessionRepository, sessionGrid);
                add(dialog);
            });

            Button deleteSession = new Button(new Icon(VaadinIcon.TRASH));
            deleteSession.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> sessionGrid.getSelectedItems().forEach(partySession -> {
                sessionList.remove(partySession);
                sessionGrid.setItems(sessionList);
                partySessionRepository.delete(partySession);
            }));

            operationalButtons.add(newSession, deleteSession);

            add(operationalButtons);
        }

    }

}
