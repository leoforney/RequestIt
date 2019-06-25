package tk.leoforney.ourrequest.rest;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.leoforney.ourrequest.model.PartySession;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.model.User;
import tk.leoforney.ourrequest.repository.PartySessionRepository;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/party")
public class SearchController {

    @Autowired
    private SpotifyAuthRepository spotifyAuthRepository;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private PartySessionRepository partySessionRepository;

    private OkHttpClient client;

    public SearchController() {
        client = new OkHttpClient();
    }

    @RequestMapping(value = {"/request/{sessionId}"})
    public String requestSong(@PathVariable("sessionId") String sessionId,
                              @RequestHeader("name") String songName) {
        PartySession partySession = partySessionRepository.findBy_id(sessionId);

        if (partySession != null) {
            SpotifyAuthorization spotifyAuth = spotifyAuthRepository.findByEmail(partySession.getEmail());

            List<Track> searchedSongs = searchSongs(spotifyAuth, songName);

            if (searchedSongs.size() > 0) {
                partySession.requestTrack(searchedSongs.get(0));
                partySessionRepository.save(partySession);

                return "Added " + searchedSongs.get(0).getName() + " to the queue.";
            } else {
                return "Failed to find songs";
            }
        } else {
            return "Session doesn't exist";
        }

    }

    @RequestMapping("/search")
    public List<Track> searchSong(@RequestHeader("name") String songName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        System.out.println(user.getEmail());

        SpotifyAuthorization sa = spotifyAuthRepository.findByEmail(user.getEmail());

        List<Track> songs = searchSongs(sa, songName);

        if (songs.size() > 0) {
            return songs;
        } else {
            return new ArrayList<>();
        }
    }

    public PlaylistSimplified[] getPlaylists(SpotifyAuthorization authorization) {

        SpotifyApi spotifyApi = getSpotifyApi(authorization);

        try {
            Paging<PlaylistSimplified> playlists = spotifyApi.getListOfCurrentUsersPlaylists().build().execute();
            return playlists.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<tk.leoforney.ourrequest.model.spotify.Track> getTracksInPlaylist(SpotifyAuthorization authorization, PlaylistSimplified playlist) {
        SpotifyApi spotifyApi = getSpotifyApi(authorization);

        try {
            int totalSize = playlist.getTracks().getTotal();
            int totalIterations = Math.round(totalSize / 100);
            System.out.println(totalSize/100);
            List<tk.leoforney.ourrequest.model.spotify.Track> tracksConverted = new ArrayList<>();

            for (int i = 0; i <= totalIterations; i++) {
                Paging<PlaylistTrack> pts = spotifyApi.getPlaylistsTracks(playlist.getId()).offset((i) * 100).build().execute();
                for (PlaylistTrack track : pts.getItems()) {
                    if (track != null) {
                        if (track.getTrack().getIsPlayable() == null || track.getTrack().getIsPlayable()) {
                            tracksConverted.add(new tk.leoforney.ourrequest.model.spotify.Track(track.getTrack()));
                        }
                    }
                }
            }
            return tracksConverted;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<Track> searchSongs(SpotifyAuthorization authorization, String songName) {

        SpotifyApi spotifyApi = getSpotifyApi(authorization);

        try {
            return Arrays.asList(spotifyApi.searchTracks(songName).limit(5).build().execute().getItems());
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public SpotifyApi getSpotifyApi(SpotifyAuthorization authorization) {
        refreshAuthorizationIfNeeded(authorization);

        return new SpotifyApi.Builder()
                .setAccessToken(authorization.getToken().getAccess_token()).build();
    }

    public SpotifyAuthorization refreshAuthorizationIfNeeded(SpotifyAuthorization authorization) {
        if (!authorization.isValid()) {
            userService.refreshAccessToken(authorization.getEmail());
            authorization = spotifyAuthRepository.findByEmail(authorization.getEmail());
        }
        return authorization;
    }

}
