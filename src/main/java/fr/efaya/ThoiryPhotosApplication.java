package fr.efaya;

import fr.efaya.domain.User;
import fr.efaya.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ThoiryPhotosApplication implements CommandLineRunner {

    @Autowired
    private UsersRepository usersRepository;

	public static void main(String[] args) {
		SpringApplication.run(ThoiryPhotosApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        List<User> users = usersRepository.findAll();
        if (users.isEmpty()) {
            User defaultUser = new User("admin", "2319efaya");
            defaultUser.setRoles(Arrays.asList("ADMIN", "USER"));
            usersRepository.save(defaultUser);
        }
    }
}
