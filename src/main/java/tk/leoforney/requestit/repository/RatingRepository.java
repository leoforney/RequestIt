package tk.leoforney.requestit.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tk.leoforney.requestit.model.Rating;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

    Rating findRatingById(ObjectId id);

}
