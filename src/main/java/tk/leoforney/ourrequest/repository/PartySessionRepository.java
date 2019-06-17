package tk.leoforney.ourrequest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tk.leoforney.ourrequest.model.PartySession;

import java.util.List;

@Repository
public interface PartySessionRepository extends MongoRepository<PartySession, String> {

    PartySession findByEmail(String email);
    PartySession findBy_id(String _id);
    List<PartySession> findAllByEmail(String email);

}
