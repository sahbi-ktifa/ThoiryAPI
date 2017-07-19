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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ThoiryPhotosApplication implements CommandLineRunner {

    @Autowired
    private List<Initializer> initializers;
    private static boolean initialized;

	public static void main(String[] args) {
		SpringApplication.run(ThoiryPhotosApplication.class, args);
	}

    @Scheduled(fixedRate=1500000)
    public void keepMeAlive() {
	    if (initialized && LocalTime.now().getHour() > 19) {
            return;//Do not keep alive
        }
        if (initializers != null) {
            initialized = true;
            initializers.forEach((initializer) -> {
                try {
                    initializer.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void run(String... args) throws Exception {
        keepMeAlive();
    }
}
