package tk.leoforney.requestit.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tk.leoforney.requestit.configuration.LocalDateAdapter;
import tk.leoforney.requestit.model.SessionChangeEvent;
import tk.leoforney.requestit.repository.PartySessionRepository;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class SocketController {

    private static final Logger log = Logger.getLogger(SocketController.class.getName());
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PartySessionRepository partySessionRepository;

    private Gson gson;

    public SocketController() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public void sessionChanged(SessionChangeEvent changeEvent) {
        log.log(Level.INFO, "Session changed: " + changeEvent.getPartySession().getId());
        this.simpMessagingTemplate.convertAndSend("/sessionSocket/" + changeEvent.getPartySession().getId(), gson.toJson(changeEvent));
    }

}
