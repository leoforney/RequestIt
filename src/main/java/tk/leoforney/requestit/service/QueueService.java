package tk.leoforney.requestit.service;

import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tk.leoforney.requestit.model.PartySession;
import tk.leoforney.requestit.model.SpotifyAuthorization;
import tk.leoforney.requestit.model.spotify.Track;
import tk.leoforney.requestit.repository.PartySessionRepository;
import tk.leoforney.requestit.repository.SpotifyAuthRepository;
import tk.leoforney.requestit.rest.SearchController;

import java.util.List;

@Service
@Repository
public class QueueService {

    private final MongoTemplate mongoTemplate;
    @Autowired
    private PartySessionRepository partySessionRepository;
    @Autowired
    private SpotifyAuthRepository spotifyAuthRepository;
    @Autowired
    private SearchController searchController;
    @Autowired
    private PartySessionRepository partySessions;

    @Autowired
    public QueueService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean queueSong(PartySession session, String songName) {
        SpotifyAuthorization sa = spotifyAuthRepository.findByEmail(session.getEmail());

        List<com.wrapper.spotify.model_objects.specification.Track> searchList = searchController.searchSongs(sa, songName);
        //session.getRequestedTracks();

        com.wrapper.spotify.model_objects.specification.Track topSelectedSong = searchList.get(0);

        return queueSong(session, topSelectedSong);
    }
    public boolean queueSong(PartySession session, com.wrapper.spotify.model_objects.specification.Track selectedTrack) {
        return queueSong(session, new Track(selectedTrack));
    }
    public boolean queueSong(PartySession session, Track selectedTrack) {

        List<Track> queuedList = partySessions.findBy_id(session.getId()).getRequestedTracks();

        boolean inQueue = false;
        for (Track track : queuedList) {
            if (selectedTrack.getId().equals(track.getId())) {
                inQueue = true;
            }
        }
        if (!inQueue) {
            session.requestTrack(selectedTrack);
            partySessionRepository.save(session);
        }
        return !inQueue;

    }

    public List<Track> getQueueFromPlaylist(PartySession session, PlaylistSimplified playlist) {
        return null;
    }

}