package fr.efaya.repository;

import fr.efaya.domain.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sktifa on 25/11/2016.
 */
@Repository
public interface AnimalsRepository extends MongoRepository<Animal, String> {
}
