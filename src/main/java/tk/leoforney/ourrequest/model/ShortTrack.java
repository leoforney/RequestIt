package tk.leoforney.ourrequest.model;

import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;

public class ShortTrack {
    private AlbumSimplified album;
    private ArtistSimplified[] artists;
    //private CountryCode[] availableMarkets;
    private Integer durationMs;

    /*
    public AlbumSimplified getAlbum() {
        return album;
    }

    public void setAlbum(AlbumSimplified album) {
        this.album = album;
    }

    public ArtistSimplified[] getArtists() {
        return artists;
    }

    public void setArtists(ArtistSimplified[] artists) {
        this.artists = artists;
    }

    public CountryCode[] getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(CountryCode[] availableMarkets) {
        this.availableMarkets = availableMarkets;
    }*/
    private Boolean explicit;
    private String href;
    private String id;
    private String name;
    private Integer popularity;
    private boolean added;

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

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

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public void updateTrack (Track track) {
        this.explicit = track.getIsExplicit();
        this.href = track.getHref();
        this.id = track.getId();
        this.name = track.getName();
        this.popularity = track.getPopularity();
        this.durationMs = track.getDurationMs();
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }
}
