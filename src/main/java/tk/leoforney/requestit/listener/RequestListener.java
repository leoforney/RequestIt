package tk.leoforney.requestit.listener;

import tk.leoforney.requestit.model.spotify.Track;

public interface RequestListener {

    void trackRequested(Track addedTrack);
    void trackAccepted(Track track);

}
