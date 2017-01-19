package fr.efaya.api.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.efaya.domain.Animal;
import fr.efaya.domain.Animals;
import fr.efaya.repository.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by sktifa on 19/01/2017.
 */
@Service
public class AnimalsInitializer implements Initializer{

    @Autowired
    private AnimalsRepository repository;

    @Override
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Animals animals = mapper.readValue(this.getClass().getResourceAsStream("animals.json"), Animals.class);
        animals.getAnimals()
                .forEach(a -> {
                    Animal animal = repository.findOne(a.getId());
                    if (animal == null) {
                        repository.save(a);
                    }
                });
    }
}
