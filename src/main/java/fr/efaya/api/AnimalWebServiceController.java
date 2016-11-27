package fr.efaya.api;

import fr.efaya.domain.Animal;
import fr.efaya.repository.service.AnimalsService;
import fr.efaya.repository.service.CommonObjectNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@RestController
@RequestMapping("api/animal")
public class AnimalWebServiceController {

    @Autowired
    private AnimalsService animalsService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Animal> retrieveAllAnimals() {
        return animalsService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Animal createAnimal(@RequestBody @Valid Animal animal) throws CommonObjectNotFound {
        return save(animal, null);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Animal saveAnimal(@PathVariable(required = false) String id,
                             @RequestBody @Valid Animal animal) throws CommonObjectNotFound {
        return save(animal, id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Animal deleteAnimal(@PathVariable(required = false) String id) throws CommonObjectNotFound {
        return animalsService.delete(id);
    }

    private Animal save(Animal animal, String id) throws CommonObjectNotFound {
        animal.setId(id);
        return animalsService.save(id, animal);
    }
}
