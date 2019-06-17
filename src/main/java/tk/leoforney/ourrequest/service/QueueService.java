package tk.leoforney.ourrequest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.model.spotify.Track;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.rest.SearchController;

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
        List<Track> queuedList = partySessions.findBy_id(session.getId()).getRequestedTracks();
                //session.getRequestedTracks();

        com.wrapper.spotify.model_objects.specification.Track topSelectedSong = searchList.get(0);

        boolean inQueue = false;
        for (Track track : queuedList) {
            if (topSelectedSong.getId().equals(track.getId())) {
                inQueue = true;
            }
        }
        if (!inQueue) {
            session.requestTrack(topSelectedSong);
            partySessionRepository.save(session);
        }
        return !inQueue;

    }

}
