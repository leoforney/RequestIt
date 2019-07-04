
package tk.leoforney.requestit.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @Override
    public String toString() {
        return "Context{" +
                "uri=" + uri +
                ", metadata=" + metadata +
                '}';
    }

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