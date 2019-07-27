package tk.leoforney.requestit.client.service;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public AuthenticationService() {

    }

    public boolean authenticated() {
        HttpResponse<JsonNode> userResponse = Unirest.get("http://localhost:8080/user")
                .asJson();
        if (userResponse.getBody().getObject().has("authenticated")) {
            return userResponse.getBody().getObject().getBoolean("authenticated");
        } else{
            return false;
        }

    }

    public boolean dataPersisted() {
        return (email != null && password != null);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String email, password;


}
