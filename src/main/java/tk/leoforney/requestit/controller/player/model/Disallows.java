
package tk.leoforney.requestit.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Disallows {

    @SerializedName("pausing")
    @Expose
    private Boolean pausing;

    @Override
    public String toString() {
        return "Disallows{" +
                "pausing=" + pausing +
                ", skippingPrev=" + skippingPrev +
                '}';
    }

    @SerializedName("skipping_prev")
    @Expose
    private Boolean skippingPrev;

    public Boolean getPausing() {
        return pausing;
    }

    public void setPausing(Boolean pausing) {
        this.pausing = pausing;
    }

    public Boolean getSkippingPrev() {
        return skippingPrev;
    }

    public void setSkippingPrev(Boolean skippingPrev) {
        this.skippingPrev = skippingPrev;
    }

}
