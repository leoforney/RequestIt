package tk.leoforney.requestit.client.stomp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import tk.leoforney.requestit.configuration.LocalDateAdapter;
import tk.leoforney.requestit.model.SessionChangeEvent;
import tk.leoforney.requestit.spotify.Track;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.logging.Logger;

public class SessionHandler implements StompSessionHandler {

    private final static Logger logger = Logger.getLogger(SessionHandler.class.getName());
    public StompSession session;
    private Gson gson;

    public SessionHandler() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
        logger.info("New session established: " + session.getSessionId());
        session.subscribe("/sessionSocket/leog19", this);
        logger.info("Subscribed to " + "/sessionSocket/leog19");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if (headers.getDestination().equals("/sessionSocket/leog19")) {
            SessionChangeEvent changeEvent = gson.fromJson((String) payload, SessionChangeEvent.class);
            Track addedTrack = changeEvent.getAddedTrack();
            Track requestedTrack = changeEvent.getRequestedTrack();
            if (addedTrack != null) {
                logger.info("Added track: " + addedTrack.getName() + " - " + addedTrack.getArtist().getName());
            }
            if (requestedTrack != null) {
                logger.info("Requested track: " + requestedTrack.getName() + " - " + requestedTrack.getArtist().getName());
            }
        }
    }
}
