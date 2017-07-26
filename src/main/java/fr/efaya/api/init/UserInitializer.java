package fr.efaya.api.init;

import fr.efaya.domain.User;
import fr.efaya.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sktifa on 19/01/2017.
 */
@Service
public class UserInitializer implements Initializer {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void load() {
        List<User> users = usersRepository.findAll();
        if (users.isEmpty()) {
            User defaultUser = new User("admin", "thoiryadm");
            defaultUser.setRoles(Arrays.asList("ADMIN", "USER"));
            usersRepository.save(defaultUser);
        }
    }
}
