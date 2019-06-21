
package tk.leoforney.ourrequest.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentTrack {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("linked_from_uri")
    @Expose
    private Object linkedFromUri;
    @SerializedName("linked_from")
    @Expose
    private LinkedFrom linkedFrom;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("duration_ms")
    @Expose
    private Integer durationMs;
    @SerializedName("artists")
    @Expose
    private List<Artist> artists = null;
    @SerializedName("album")
    @Expose
    private Album album;

    @Override
    public String toString() {
        return "CurrentTrack{" +
                "id='" + id + '\'' +
                ", uri='" + uri + '\'' +
                ", type='" + type + '\'' +
                ", linkedFromUri=" + linkedFromUri +
                ", linkedFrom=" + linkedFrom +
                ", mediaType='" + mediaType + '\'' +
                ", name='" + name + '\'' +
                ", durationMs=" + durationMs +
                ", artists=" + artists +
                ", album=" + album +
                ", isPlayable=" + isPlayable +
                '}';
    }

    @SerializedName("is_playable")
    @Expose
    private Boolean isPlayable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getLinkedFromUri() {
        return linkedFromUri;
    }

    public void setLinkedFromUri(Object linkedFromUri) {
        this.linkedFromUri = linkedFromUri;
    }

    public LinkedFrom getLinkedFrom() {
        return linkedFrom;
    }

    public void setLinkedFrom(LinkedFrom linkedFrom) {
        this.linkedFrom = linkedFrom;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Boolean getIsPlayable() {
        return isPlayable;
    }

    public void setIsPlayable(Boolean isPlayable) {
        this.isPlayable = isPlayable;
    }

}
