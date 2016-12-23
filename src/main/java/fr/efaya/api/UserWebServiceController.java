package fr.efaya.api;

import fr.efaya.domain.User;
import fr.efaya.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by sktifa on 22/12/2016.
 */
@RestController
public class UserWebServiceController {

    @Autowired
    UsersRepository usersRepository;

    @RequestMapping(value = "common/register", method = RequestMethod.POST, produces = "text/plain")
    public String registration(@RequestBody String username) throws AlreadyExistingUserException {
        User alreadyExist = usersRepository.findByUsername(username);
        if (alreadyExist != null) {
            throw new AlreadyExistingUserException();
        }
        String password = UUID.randomUUID().toString();
        User user = new User(username, password);
        usersRepository.save(user);
        return password;
    }

    @ResponseStatus(value= HttpStatus.CONFLICT, reason="Un utilisateur avec ce nom existe déjà.")
    private class AlreadyExistingUserException extends Throwable  {
    }
}
