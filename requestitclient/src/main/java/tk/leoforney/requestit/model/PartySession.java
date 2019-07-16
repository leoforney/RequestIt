package tk.leoforney.requestit.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import tk.leoforney.requestit.spotify.Track;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PartySession {

    private String _id;
    private String email;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expiration;

    private String sessionName;
    private transient List<Track> requestedTracks;
    private transient List<Track> acceptedTracks;

    public PartySession() {
    }

    private PartySession(PartySession session) {
        this.expiration = session.expiration;
        this.sessionName = session.sessionName;
        this._id = session._id;
        this.email = session.email;
        this.acceptedTracks = session.acceptedTracks;
        this.requestedTracks = session.requestedTracks;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Track> getAcceptedTracks() {
        if (acceptedTracks == null) acceptedTracks = new ArrayList<>();
        return acceptedTracks;
    }

    public void setAcceptedTracks(List<Track> acceptedTracks) {
        this.acceptedTracks = acceptedTracks;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public List<Track> getRequestedTracks() {
        if (requestedTracks == null) requestedTracks = new ArrayList<>();
        return requestedTracks;
    }

    public boolean isValid() {
        return LocalDate.now().isBefore(expiration);
    }

    public void setRequestedTracks(List<Track> requestedTracks) {
        this.requestedTracks = requestedTracks;
    }

    public void requestTrack(Track track) {
    }

    public void acceptTrack(Track track) {

    }

    public void deleteRequestedTrack(Track track) {
        requestedTracks = getRequestedTracks();
        for (int i = 0; i < requestedTracks.size(); i++) {
            if (requestedTracks.get(i).getId().equals(track.getId())) {
                requestedTracks.remove(i);
            }
        }
        setRequestedTracks(requestedTracks);
    }

    public PartySession clone() {
        return new PartySession(this);
    }
}
