package tk.leoforney.ourrequest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tk.leoforney.ourrequest.model.SpotifyAuthorization;

@Repository
public interface SpotifyAuthRepository extends MongoRepository<SpotifyAuthorization, String> {

    SpotifyAuthorization findByEmail(String email);

}
