package tk.leoforney.requestit.model;

import org.springframework.lang.Nullable;
import tk.leoforney.requestit.spotify.Track;

public class SessionChangeEvent {

    public SessionChangeEvent() {

    }

    public SessionChangeEvent(@Nullable Track addedTrack, @Nullable Track requestedTrack) {
        this.addedTrack = addedTrack;
        this.requestedTrack = requestedTrack;
    }

    public Track getAddedTrack() {
        return addedTrack;
    }

    public void setAddedTrack(Track addedTrack) {
        this.addedTrack = addedTrack;
    }

    public Track getRequestedTrack() {
        return requestedTrack;
    }

    public void setRequestedTrack(Track requestedTrack) {
        this.requestedTrack = requestedTrack;
    }

    public PartySession getPartySession() {
        return partySession;
    }

    public void setPartySession(PartySession partySession) {
        this.partySession = partySession;
    }

    private Track addedTrack, requestedTrack;
    private PartySession partySession;

}
