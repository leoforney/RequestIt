package tk.leoforney.requestit.listener;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import tk.leoforney.requestit.model.spotify.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestNotifier {

    private List<RequestListener> listeners;

    private static final Logger logger = Logger.getLogger(RequestNotifier.class.getName());

    public RequestNotifier() {
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
        for (RequestListener listener : listeners) {
            listener.trackRequested(track);
        }
    }

    public void songAccepted(Track track) {
        for (RequestListener listener : listeners) {
            listener.trackAccepted(track);
        }
    }
}
