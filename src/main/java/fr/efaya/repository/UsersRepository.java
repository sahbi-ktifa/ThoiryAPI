package fr.efaya.repository;

import fr.efaya.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sktifa on 21/12/2016.
 */
@Repository
public interface UsersRepository extends MongoRepository<User, String> {
    User findByUsername(String name);
}
