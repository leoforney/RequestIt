package tk.leoforney.requestit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import tk.leoforney.requestit.listener.RequestNotifier;
import tk.leoforney.requestit.model.spotify.Track;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "partySessions")
public class PartySession {

    @Id
    private String _id;
    private String email;
    private LocalDate expiration;
    private String sessionName;
    private transient List<Track> requestedTracks;
    private transient List<Track> acceptedTracks;

    public RequestNotifier getNotifier() {
        if (notifierMap == null) {
            notifierMap = new HashMap<>();
        }
        if (!notifierMap.containsKey(_id)) {
            RequestNotifier notifier = new RequestNotifier();
            notifierMap.put(_id, notifier);
            return notifier;
        } else {
            return notifierMap.get(_id);
        }
    }

    @Transient
    private static Map<String, RequestNotifier> notifierMap;

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

    public void requestTrack(com.wrapper.spotify.model_objects.specification.Track track) {
        requestTrack(new Track(track));
    }

    public void requestTrack(Track track) {
        getNotifier().songRequested(track);
        List<Track> requestedTracksModified = getRequestedTracks();
        requestedTracksModified.add(track);
        setRequestedTracks(requestedTracksModified);
    }

    public void acceptTrack(Track track) {
        getNotifier().songAccepted(track);
        List<Track> acceptedTracksModified = getAcceptedTracks();
        deleteRequestedTrack(track);
        acceptedTracksModified.add(track);
        setAcceptedTracks(acceptedTracksModified);
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
