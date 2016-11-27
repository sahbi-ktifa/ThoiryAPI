package fr.efaya.repository;

import fr.efaya.domain.Picture;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sktifa on 25/11/2016.
 */
@Repository
public interface PicturesRepository extends MongoRepository<Picture, String> {
}
