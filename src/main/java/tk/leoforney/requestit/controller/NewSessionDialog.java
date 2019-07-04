package tk.leoforney.requestit.controller;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tk.leoforney.requestit.model.PartySession;
import tk.leoforney.requestit.model.User;
import tk.leoforney.requestit.repository.PartySessionRepository;
import tk.leoforney.requestit.repository.SpotifyAuthRepository;
import tk.leoforney.requestit.service.CustomUserDetailsService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class NewSessionDialog extends Dialog {

    private PartySessionRepository partySessionRepository;

    public NewSessionDialog(CustomUserDetailsService userService,
                            SpotifyAuthRepository spotifyAuthRepository,
                            PartySessionRepository partySessionRepository,
                            Grid<PartySession> sessionGrid) {
        this.partySessionRepository = partySessionRepository;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Binder<PartySession> binder = new Binder<>(PartySession.class);

        Dialog dialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(new H3("Create new session"));

        Label formStatusLabel = new Label();
        binder.setStatusLabel(formStatusLabel);

        PartySession emptySession = new PartySession();
        emptySession.setEmail(user.getEmail());
        emptySession.setAcceptedTracks(new ArrayList<>());
        emptySession.setRequestedTracks(new ArrayList<>());

        binder.setBean(emptySession);

        TextField sessionNameTextField = new TextField("Session Name");
        sessionNameTextField.setPlaceholder("Joe's wedding");
        binder.forField(sessionNameTextField).asRequired("Session name can't be empty!")
                .bind(PartySession::getSessionName, PartySession::setSessionName);

        TextField sessionIdTextField = new TextField("Session ID");
        sessionIdTextField.setPlaceholder("joereq19");
        binder.forField(sessionIdTextField)
                .withValidator(name -> name.length() < 20,
                        "Session name must be shorter than 20 characters")
                .withValidator(value -> (partySessionRepository.findBy_id(value) == null),
                        "Session ID already exists")
                .bind(PartySession::getId, PartySession::setId);


        DatePicker datePicker = new DatePicker("Expiration date");
        datePicker.setInitialPosition(LocalDate.now().plus(2, ChronoUnit.DAYS));
        binder.forField(datePicker).asRequired("Expiration date is required")
                .bind(PartySession::getExpiration, PartySession::setExpiration);

        dialogLayout.add(sessionNameTextField, sessionIdTextField, datePicker);

        dialog.setWidth("400px");

        Button confirmButton = new Button("Confirm", event -> {
            if (!binder.validate().hasErrors()) {
                PartySession session = binder.getBean().clone();

                if (sessionIdTextField.getValue().equals("")) {
                    session.setId(new ObjectId().toString());
                }

                partySessionRepository.save(session);
                sessionGrid.setItems(partySessionRepository.findAllByEmail(user.getEmail()));
                dialog.close();
            }

        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(confirmButton, cancelButton);
        dialogLayout.add(footer);
        dialogLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, footer);

        dialog.add(dialogLayout);

        dialog.open();

    }

}
