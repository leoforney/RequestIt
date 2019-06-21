
package tk.leoforney.ourrequest.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedFrom {

    @Override
    public String toString() {
        return "LinkedFrom{" +
                "uri=" + uri +
                ", id=" + id +
                '}';
    }

    @SerializedName("uri")
    @Expose
    private Object uri;
    @SerializedName("id")
    @Expose
    private Object id;

    public Object getUri() {
        return uri;
    }

    public void setUri(Object uri) {
        this.uri = uri;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

}
