
package tk.leoforney.ourrequest.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("uri")
    @Expose
    private Object uri;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public Object getUri() {
        return uri;
    }

    public void setUri(Object uri) {
        this.uri = uri;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
