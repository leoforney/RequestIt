
package tk.leoforney.ourrequest.controller.player.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restrictions {

    @Override
    public String toString() {
        return "Restrictions{" +
                "disallowPausingReasons=" + disallowPausingReasons +
                ", disallowSkippingPrevReasons=" + disallowSkippingPrevReasons +
                '}';
    }

    @SerializedName("disallow_pausing_reasons")
    @Expose
    private List<String> disallowPausingReasons = null;
    @SerializedName("disallow_skipping_prev_reasons")
    @Expose
    private List<String> disallowSkippingPrevReasons = null;

    public List<String> getDisallowPausingReasons() {
        return disallowPausingReasons;
    }

    public void setDisallowPausingReasons(List<String> disallowPausingReasons) {
        this.disallowPausingReasons = disallowPausingReasons;
    }

    public List<String> getDisallowSkippingPrevReasons() {
        return disallowSkippingPrevReasons;
    }

    public void setDisallowSkippingPrevReasons(List<String> disallowSkippingPrevReasons) {
        this.disallowSkippingPrevReasons = disallowSkippingPrevReasons;
    }

}
