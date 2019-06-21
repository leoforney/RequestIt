package tk.leoforney.ourrequest.controller.player;

import com.google.gson.Gson;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import tk.leoforney.ourrequest.controller.player.model.SpotifyWebState;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.model.spotify.Track;

import java.util.ArrayList;
import java.util.List;

@Tag("div")
@JavaScript("frontend://js/jquery-3.4.1.min.js")
@HtmlImport("frontend://html/player.html")
public class PlayerComponent extends Component {

    Gson gson;

    public PlayerComponent(Component component, SpotifyAuthorization authorization) {
        gson = new Gson();
        component.addAttachListener(new ComponentEventListener<AttachEvent>() {
            @Override
            public void onComponentEvent(AttachEvent event) {
                setToken(authorization.getToken().getAccess_token());
                event.getUI().getPage().addJavaScript("https://sdk.scdn.co/spotify-player.js");
            }
        });
        setId("playerComponent");
    }

    private List<StateChangeCallback> callbackList;

    public void addCallback(StateChangeCallback callback) {
        if (callbackList == null) {
            callbackList = new ArrayList<>();
        }
        callbackList.add(callback);
    }

    public void playSong(String uri) {
        getElement().executeJavaScript("play('" + uri + "\"')");
    }

    public void playSong(List<Track> trackList) {

    }

    public void playSong(Track track) {
        playSong(track.getUri());
    }

    public void pause() {
        getElement().executeJavaScript("pause()");
    }

    public void togglePlayback() {
        getElement().executeJavaScript("toggle()");
    }

    public void setVolume(double value) {
        getElement().executeJavaScript("setVolume(" + value + ")");
    }

    private String deviceId;

    @ClientCallable
    public void setDeviceId(String device_id) {
        this.deviceId = deviceId;
    }

    private String state = "{}";

    @ClientCallable
    public void setState(String state) {
        this.state = state;
        if (callbackList != null) {
            for (StateChangeCallback callback: callbackList) {
                callback.stateChanged(getState());
            }
        }
    }

    public SpotifyWebState getState() {
        return gson.fromJson(state, SpotifyWebState.class);
    }

    public String getDeviceId() {return deviceId;}

    public void setToken(String token) {
        getElement().executeJavaScript("setToken('" + token + "')");
    }

    interface StateChangeCallback {
        void stateChanged(SpotifyWebState state);
    }

}