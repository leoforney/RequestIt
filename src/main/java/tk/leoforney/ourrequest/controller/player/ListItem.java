package tk.leoforney.ourrequest.controller.player;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import tk.leoforney.ourrequest.model.spotify.Track;

public class ListItem extends Div {

    private Track storedTrack;
    Html songName;

    public ListItem(Track track, boolean requested) {
        this.storedTrack = track;
        if (track != null) {
            songName = new Html("<p uri=\"" + track.getUri() + "\" id=\"" + track.getId() + "\" class=\"sortable-item\" style=\"width: 100%;\">" + track.getArtist().getName() + " - " + track.getName() + "</p>");
            if (requested) {
                songName = new Html("<p uri=\"" + track.getUri() + "\" id=\"" + track.getId() + "\" class=\"sortable-item requested\" style=\"width: 100%;\">" + track.getArtist().getName() + " - " + track.getName() + "</p>");
            }
        }
        add(songName);
    }

    public ListItem() {
        new ListItem(null, false);
    }

}
