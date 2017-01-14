package fr.efaya.api;

import fr.efaya.domain.User;
import fr.efaya.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sktifa on 22/12/2016.
 */
@RestController
@CrossOrigin
public class UserWebServiceController {

    @Autowired
    UsersRepository usersRepository;

    @RequestMapping(value = "api/user", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> retrieveUsers() {
        return usersRepository.findAll();
    }

    @RequestMapping(value = "common/register", method = RequestMethod.POST)
    public String registration(@RequestBody String username, HttpServletResponse response) throws AlreadyExistingUserException {
        User alreadyExist = usersRepository.findByUsername(username);
        if (alreadyExist != null) {
            throw new AlreadyExistingUserException();
        }
        String password = UUID.randomUUID().toString();
        User user = new User(username, password);
        user.setCreationDate(new Date());
        usersRepository.save(user);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        return password;
    }

    public class AlreadyExistingUserException extends Throwable  {
    }
}
