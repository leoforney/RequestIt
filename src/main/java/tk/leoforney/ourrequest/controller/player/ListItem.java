package tk.leoforney.ourrequest.controller.player;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import tk.leoforney.ourrequest.model.spotify.Track;

public class ListItem extends Div {

    private Track storedTrack;
    Html songName;

    public ListItem(Track track) {
        this.storedTrack = track;
        if (track != null) {
            songName = new Html("<div><p>" + track.getName() + " - " + track.getArtist().getName() + "</p></div>");
        } else {
            songName = new Html("<div><p>" + "Baby on Baby" + " - " + "DaBaby" + "</p></div>");
        }
        add(songName);
    }

    public ListItem() {
        new ListItem(null);
    }

}
