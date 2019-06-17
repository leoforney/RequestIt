package tk.leoforney.ourrequest.model.spotify;

public class Track {

    private String id, name, previewUrl, type;
    private int durationMs, popularity;
    private boolean explicit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    private Artist artist;
    private Album album;

    public Track() {

    }

    public Track(com.wrapper.spotify.model_objects.specification.Track track) {
        id = track.getId();
        name = track.getName();
        previewUrl = track.getPreviewUrl();
        type = track.getType().getType();
        durationMs = track.getDurationMs();
        popularity = track.getPopularity();
        explicit = track.getIsExplicit();
        artist = new Artist(track.getArtists()[0]);
        album = new Album(track.getAlbum());
    }


}
