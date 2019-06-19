package tk.leoforney.ourrequest.controller.player;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.model.spotify.Track;

@Tag("div")
@JavaScript("frontend://js/jquery-3.4.1.min.js")
@HtmlImport("frontend://html/player.html")
public class PlayerComponent extends Component {

    public PlayerComponent(Component component, SpotifyAuthorization authorization) {
        component.addAttachListener(new ComponentEventListener<AttachEvent>() {
            @Override
            public void onComponentEvent(AttachEvent event) {
                setToken(authorization.getToken().getAccess_token());
                event.getUI().getPage().addJavaScript("https://sdk.scdn.co/spotify-player.js");
            }
        });
        setId("playerComponent");
    }

    public void playSong(String uri) {
        getElement().executeJavaScript("play('" + uri + "')");

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

    public void setToken(String token) {
        getElement().executeJavaScript("setToken('" + token + "')");
    }

}