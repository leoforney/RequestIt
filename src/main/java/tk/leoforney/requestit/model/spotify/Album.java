package tk.leoforney.requestit.model.spotify;

public class Album {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private String albumType;
    private String name;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    private Artist artist;

    public Album() {}
    public Album(com.wrapper.spotify.model_objects.specification.AlbumSimplified album) {
        albumType = album.getAlbumType().getType();
        name = album.getName();
        id = album.getId();
        artist = new Artist(album.getArtists()[0]);
    }

}
