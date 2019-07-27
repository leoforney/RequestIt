package tk.leoforney.requestit.rest;


import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.leoforney.requestit.model.PartySession;
import tk.leoforney.requestit.model.SpotifyAuthorization;
import tk.leoforney.requestit.model.User;
import tk.leoforney.requestit.model.spotify.Track;
import tk.leoforney.requestit.repository.PartySessionRepository;
import tk.leoforney.requestit.repository.SpotifyAuthRepository;
import tk.leoforney.requestit.service.CustomUserDetailsService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/session")
public class SessionController {

    @Autowired
    private PartySessionRepository partySessionRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private SpotifyAuthRepository spotifyAuthRepository;

    @Autowired
    private SearchController searchController;


    public SessionController() {

    }

    @GetMapping("/getRequestedTracks")
    public List<Track> getRequestedTracks(@RequestHeader String sessionId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = customUserDetailsService.findUserByEmail(auth.getName());

        PartySession partySession = partySessionRepository.findBy_id(sessionId);

        // TODO: Implement party sharing across different emails
        if (partySession.getEmail().equals(user.getEmail())) {
            return partySession.getRequestedTracks();
        } else {
            return null;
        }

    }

    @GetMapping("/refreshSession")
    public String refreshSession(@RequestHeader String sessionId) throws IOException, SpotifyWebApiException {
        PartySession partySession = partySessionRepository.findBy_id(sessionId);

        SpotifyAuthorization sa = spotifyAuthRepository.findByEmail(partySession.getEmail());

        SpotifyApi spotifyApi = searchController.getSpotifyApi(sa);

        List<Track> requestedTracks = partySession.getRequestedTracks();
        List<Track> acceptedTracks = partySession.getAcceptedTracks();

        // TODO: Finish, refresh list
        for (Track track : requestedTracks) {
            requestedTracks.set(requestedTracks.indexOf(track),
                    new Track(spotifyApi.getTrack(track.getId()).build().execute()));
        }

        for (Track track : acceptedTracks) {
            acceptedTracks.set(acceptedTracks.indexOf(track),
                    new Track(spotifyApi.getTrack(track.getId()).build().execute()));
        }

        partySession.setAcceptedTracks(acceptedTracks);
        partySession.setRequestedTracks(requestedTracks);

        partySessionRepository.save(partySession);

        return "";
    }

}
