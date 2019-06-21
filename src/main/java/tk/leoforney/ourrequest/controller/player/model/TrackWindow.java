
package tk.leoforney.ourrequest.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackWindow {

    @SerializedName("current_track")
    @Expose
    private CurrentTrack currentTrack;

    @Override
    public String toString() {
        return "TrackWindow{" +
                "currentTrack=" + currentTrack +
                ", nextTracks=" + nextTracks +
                ", previousTracks=" + previousTracks +
                '}';
    }

    @SerializedName("next_tracks")
    @Expose
    private List<Object> nextTracks = null;
    @SerializedName("previous_tracks")
    @Expose
    private List<Object> previousTracks = null;

    public CurrentTrack getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(CurrentTrack currentTrack) {
        this.currentTrack = currentTrack;
    }

    public List<Object> getNextTracks() {
        return nextTracks;
    }

    public void setNextTracks(List<Object> nextTracks) {
        this.nextTracks = nextTracks;
    }

    public List<Object> getPreviousTracks() {
        return previousTracks;
    }

    public void setPreviousTracks(List<Object> previousTracks) {
        this.previousTracks = previousTracks;
    }

}
