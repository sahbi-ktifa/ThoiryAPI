package fr.efaya.api.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.efaya.domain.Specie;
import fr.efaya.domain.Species;
import fr.efaya.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by sktifa on 19/01/2017.
 */
@Service
public class SpeciesInitializer implements Initializer{

    @Autowired
    private SpeciesRepository repository;

    @Override
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Species species = mapper.readValue(this.getClass().getResourceAsStream("species.json"), Species.class);
        species.getSpecies()
                .forEach(s -> {
                    Specie specie = repository.findOne(s.getId());
                    if (specie == null) {
                        repository.save(s);
                    }
                });
    }
}
