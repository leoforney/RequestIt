package tk.leoforney.requestit.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "spotifyCodes")
public class SpotifyAuthorization {

    @Id
    private ObjectId _id;
    private String email;
    private AccessToken token;
    private long timeIssued;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public AccessToken getToken() {
        return token;
    }

    public void setToken(AccessToken token) {
        this.token = token;
    }

    public long getTimeIssued() {
        return timeIssued;
    }

    public void setTimeIssued(long timeIssued) {
        this.timeIssued = timeIssued;
    }

    public boolean isValid() {
        //System.out.println(timeIssued + " -> " + (timeIssued + token.getExpires_in()*1000) + " : " + new Date().getTime());
        return timeIssued + token.getExpires_in()*1000 > new Date().getTime();
    }

}
