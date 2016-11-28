package fr.efaya.repository;

import fr.efaya.domain.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Repository
public interface PicturesRepository extends MongoRepository<Picture, String> {
    Page<Picture> findByAnimalIdsInOrSpeciesIdsInOrTitle(List<String> animalIds, List<String> speciesIds, String title, Pageable pageable);
}
