package fr.efaya;

import fr.efaya.api.init.Initializer;
import fr.efaya.domain.User;
import fr.efaya.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ThoiryPhotosApplication implements CommandLineRunner {


    @Autowired
    private List<Initializer> initializers;

	public static void main(String[] args) {
		SpringApplication.run(ThoiryPhotosApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        if (initializers != null) {
            initializers.forEach((initializer) -> {
                try {
                    initializer.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
