package tk.leoforney.requestit.service;

import com.google.gson.Gson;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tk.leoforney.requestit.model.RefreshToken;
import tk.leoforney.requestit.model.Role;
import tk.leoforney.requestit.model.SpotifyAuthorization;
import tk.leoforney.requestit.model.User;
import tk.leoforney.requestit.repository.RoleRepository;
import tk.leoforney.requestit.repository.SpotifyAuthRepository;
import tk.leoforney.requestit.repository.UserRepository;

import java.io.IOException;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SpotifyAuthRepository spotifyAuthRepository;
    private OkHttpClient client;
    private Gson gson;

    public CustomUserDetailsService() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        if (user.getRoles() == null) {
            Role userRole = roleRepository.findByRole("ADMIN");
            user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        }
        userRepository.save(user);
    }

    public SpotifyAuthorization refreshAccessToken(String email) {
        User user = userRepository.findByEmail(email);
        SpotifyAuthorization auth = spotifyAuthRepository.findByEmail(email);

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", auth.getToken().getRefresh_token())
                .build();

        String originalInput = "6bef4555fccb4908a6f5fbafcb6604f1:7837f41948e54369891d8d2ef1cec32d";
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());

        Request request = new Request.Builder().url("https://accounts.spotify.com/api/token")
                .header("Authorization", "Basic " + encodedString)
                .post(formBody).build();

        try {
            RefreshToken refreshToken = gson.fromJson(client.newCall(request).execute().body().string(), RefreshToken.class);

            auth.getToken().setAccess_token(refreshToken.getAccess_token());
            auth.setTimeIssued(new Date().getTime());

            spotifyAuthRepository.save(auth);

            return auth;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}