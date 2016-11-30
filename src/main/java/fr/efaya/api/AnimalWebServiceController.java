package fr.efaya.api;

import fr.efaya.domain.Animal;
import fr.efaya.domain.Picture;
import fr.efaya.domain.Specie;
import fr.efaya.repository.service.AnimalsService;
import fr.efaya.repository.service.CommonObjectNotFound;
import fr.efaya.repository.service.PicturesService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@RestController
@RequestMapping("api/animal")
public class AnimalWebServiceController {

    @Autowired
    private AnimalsService animalsService;

    @Autowired
    private PicturesService picturesService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Animal> retrieveAllAnimals() {
        return animalsService.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Animal retrieveAnimal(@PathVariable String id) throws CommonObjectNotFound {
        return animalsService.findById(id);
    }

    @RequestMapping(value = "{id}/preview", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] retrieveAnimalPreview(@PathVariable String id) throws CommonObjectNotFound, IOException {
        Picture picture = picturesService.findOneByAnimalsId(id);
        if (picture != null) {
            return picturesService.retrievePictureBinary(picture.getId());
        } else {
            return IOUtils.toByteArray(this.getClass().getResourceAsStream("Koala.jpg"));
        }
    }

    @RequestMapping(value = "{id}/siblings", method = RequestMethod.GET)
    public List<Animal> retrieveAnimalSiblings(@PathVariable String id) throws CommonObjectNotFound {
        return animalsService.retrieveSiblings(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Animal createAnimal(@RequestBody @Valid Animal animal) throws CommonObjectNotFound {
        return save(animal, null);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Animal saveAnimal(@PathVariable String id,
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
