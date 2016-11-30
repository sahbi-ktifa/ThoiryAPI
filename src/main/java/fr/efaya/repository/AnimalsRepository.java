package fr.efaya.repository;

import fr.efaya.domain.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Repository
public interface AnimalsRepository extends MongoRepository<Animal, String> {
    List<Animal> findBySpecieId(String specieId);
}
