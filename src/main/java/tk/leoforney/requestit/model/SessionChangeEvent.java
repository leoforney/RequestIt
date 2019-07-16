package tk.leoforney.requestit.model;

import tk.leoforney.requestit.model.spotify.Track;

import javax.annotation.Nullable;

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

    public SessionChangeEvent setPartySession(PartySession partySession) {
        this.partySession = partySession;
        return this;
    }

    private Track addedTrack, requestedTrack;
    private PartySession partySession;

}
