
package tk.leoforney.ourrequest.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album {

    @SerializedName("uri")
    @Expose
    private String uri;

    @Override
    public String toString() {
        return "Album{" +
                "uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", images=" + images +
                '}';
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}
