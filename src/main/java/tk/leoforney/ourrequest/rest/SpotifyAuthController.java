package tk.leoforney.ourrequest.rest;

import com.google.gson.Gson;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.leoforney.ourrequest.model.AccessToken;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;
import tk.leoforney.ourrequest.model.User;
import tk.leoforney.ourrequest.repository.SpotifyAuthRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@RestController
public class SpotifyAuthController {

    private OkHttpClient client;
    private Gson gson;

    @Autowired
    private SpotifyAuthRepository repository;

    @Autowired
    private CustomUserDetailsService userService;

    public SpotifyAuthController() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    @RequestMapping("/dashboard/spotify")
    public String authorizeCode(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameterMap().containsKey("code")) {
            RequestBody formBody = new FormBody.Builder()
                    .add("code", request.getParameter("code"))
                    .add("grant_type", "authorization_code")
                    .add("redirect_uri", "http://localhost:8080/dashboard/spotify")
                    .build();

            String originalInput = "6bef4555fccb4908a6f5fbafcb6604f1:7837f41948e54369891d8d2ef1cec32d";
            String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());

            Request tokenRequest = new Request.Builder().url("https://accounts.spotify.com/api/token")
                    .header("Authorization", "Basic " + encodedString)
                    .post(formBody).build();
            try {
                AccessToken token = gson.fromJson(client.newCall(tokenRequest).execute().body().string(), AccessToken.class);

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = userService.findUserByEmail(auth.getName());

                if (user.getId().toString().equals(request.getParameter("state"))) {

                    SpotifyAuthorization authorization = new SpotifyAuthorization();
                    authorization.setemail(user.getEmail());
                    authorization.setToken(token);
                    authorization.setTimeIssued(new Date().getTime());

                    repository.save(authorization);
                }

                response.sendRedirect(request.getContextPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return request.getRequestURI();
    }

}
