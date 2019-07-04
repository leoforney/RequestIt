package tk.leoforney.requestit.model.spotify;

public class Artist {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String id, name, type;

    public Artist() {}

    public Artist(com.wrapper.spotify.model_objects.specification.ArtistSimplified artist) {
        id = artist.getId();
        name = artist.getName();
        type = artist.getType().getType();
    }
}
