package tk.leoforney.requestit.listener;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import tk.leoforney.requestit.Application;
import tk.leoforney.requestit.model.PartySession;
import tk.leoforney.requestit.model.SessionChangeEvent;
import tk.leoforney.requestit.model.spotify.Track;
import tk.leoforney.requestit.websocket.PayloadMessage;
import tk.leoforney.requestit.websocket.SocketController;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static tk.leoforney.requestit.Application.appContext;

public class RequestNotifier {

    private List<RequestListener> listeners;

    private static final Logger logger = Logger.getLogger(RequestNotifier.class.getName());
    private SocketController socketController;
    private PartySession session;

    public RequestNotifier(PartySession session) {
        this.session = session;
        socketController = (SocketController) appContext.getAutowireCapableBeanFactory().getBean("socketController");
        listeners = new ArrayList<>();
    }

    public void addListener(RequestListener listener) {
        if (listener instanceof Component) {
            Component component = (Component) listener;
            component.getUI().ifPresent(ui ->
                    ui.addDetachListener((ComponentEventListener<DetachEvent>) event ->
                            removeListener(listener)));
            //logger.log(Level.INFO, "Initiated detach listener");
        }
        listeners.add(listener);
    }

    public void removeListener(RequestListener listener) {
        if (listener instanceof Component) {
            //Component component = (Component) listener;
            logger.log(Level.INFO, "Removed listener");
        }
        listeners.remove(listener);
    }

    public void songRequested(Track track) {
        socketController.sessionChanged(new SessionChangeEvent(null, track).setPartySession(session));
        for (RequestListener listener : listeners) {
            listener.trackRequested(track);
        }
    }

    public void songAccepted(Track track) {
        socketController.sessionChanged(new SessionChangeEvent(track, null).setPartySession(session));
        for (RequestListener listener : listeners) {
            listener.trackAccepted(track);
        }
    }
}
