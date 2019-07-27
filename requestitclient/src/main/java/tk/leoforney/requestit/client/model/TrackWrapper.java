package tk.leoforney.requestit.client.model;

import javafx.scene.image.Image;
import tk.leoforney.requestit.spotify.Track;

public class TrackWrapper {

    private Track track;

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private Image image;

    public TrackWrapper(Track track, Image image) {
        this.track = track;
        this.image = image;
    }

    public TrackWrapper(Track track) {
        this.image = new Image(track.getThumbnailUrl(),50, 50, true, false);
        this.track = track;
    }
}
