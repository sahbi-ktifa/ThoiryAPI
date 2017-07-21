package fr.efaya;

import fr.efaya.api.init.Initializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
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
