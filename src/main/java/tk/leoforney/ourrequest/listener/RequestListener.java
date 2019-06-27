package tk.leoforney.ourrequest.listener;

import tk.leoforney.ourrequest.model.spotify.Track;

public interface RequestListener {

    void trackRequested(Track addedTrack);
    void trackAccepted(Track track);

}
