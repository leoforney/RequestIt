
package tk.leoforney.ourrequest.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotifyWebState {

    @SerializedName("context")
    @Expose
    private Context context;
    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("paused")
    @Expose
    private Boolean paused;
    @SerializedName("shuffle")
    @Expose
    private Boolean shuffle;
    @SerializedName("repeat_mode")
    @Expose
    private Integer repeatMode;
    @SerializedName("track_window")
    @Expose
    private TrackWindow trackWindow;
    @SerializedName("timestamp")
    @Expose
    private Float timestamp;
    @SerializedName("restrictions")
    @Expose
    private Restrictions restrictions;

    @Override
    public String toString() {
        return "SpotifyWebState{" +
                "context=" + context +
                ", bitrate=" + bitrate +
                ", position=" + position +
                ", duration=" + duration +
                ", paused=" + paused +
                ", shuffle=" + shuffle +
                ", repeatMode=" + repeatMode +
                ", trackWindow=" + trackWindow +
                ", timestamp=" + timestamp +
                ", restrictions=" + restrictions +
                ", disallows=" + disallows +
                '}';
    }

    @SerializedName("disallows")
    @Expose
    private Disallows disallows;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getPaused() {
        return paused;
    }

    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    public Boolean getShuffle() {
        return shuffle;
    }

    public void setShuffle(Boolean shuffle) {
        this.shuffle = shuffle;
    }

    public Integer getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(Integer repeatMode) {
        this.repeatMode = repeatMode;
    }

    public TrackWindow getTrackWindow() {
        return trackWindow;
    }

    public void setTrackWindow(TrackWindow trackWindow) {
        this.trackWindow = trackWindow;
    }

    public Float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Float timestamp) {
        this.timestamp = timestamp;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public Disallows getDisallows() {
        return disallows;
    }

    public void setDisallows(Disallows disallows) {
        this.disallows = disallows;
    }

}
