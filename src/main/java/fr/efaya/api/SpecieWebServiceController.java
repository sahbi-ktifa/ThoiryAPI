package fr.efaya.api;

import fr.efaya.domain.Animal;
import fr.efaya.domain.Picture;
import fr.efaya.domain.Specie;
import fr.efaya.repository.service.AnimalsService;
import fr.efaya.repository.service.CommonObjectNotFound;
import fr.efaya.repository.service.PicturesService;
import fr.efaya.repository.service.SpeciesService;
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
@CrossOrigin
public class SpecieWebServiceController {

    @Autowired
    private SpeciesService speciesService;

    @Autowired
    private PicturesService picturesService;

    @Autowired
    private AnimalsService animalsService;

    @RequestMapping(value = "common/specie", method = RequestMethod.GET)
    public List<Specie> retrieveAllSpecies() {
        return speciesService.findAll();
    }

    @RequestMapping(value = "common/specie/{id}", method = RequestMethod.GET)
    public Specie retrieveSpecie(@PathVariable String id) throws CommonObjectNotFound {
        return speciesService.findById(id);
    }

    @RequestMapping(value = "common/specie/{id}/preview", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] retrieveSpeciePreview(@PathVariable String id) throws CommonObjectNotFound, IOException {
        Picture picture = picturesService.findOneBySpeciesId(id);
        if (picture != null) {
            return picturesService.retrievePictureBinary(picture.getId());
        } else {
            return IOUtils.toByteArray(this.getClass().getResourceAsStream("unknown_animal.png"));
        }
    }

    @RequestMapping(value = "common/specie/{id}/animals", method = RequestMethod.GET)
    public List<Animal> retrieveSpecieAnimals(@PathVariable String id) throws CommonObjectNotFound {
        Specie specie = speciesService.findById(id);
        return animalsService.findAllBySpecieId(specie.getId());
    }

    @RequestMapping(value = "api/specie", method = RequestMethod.POST)
    public Specie createSpecie(@RequestBody @Valid Specie specie) throws CommonObjectNotFound {
        return save(specie, null);
    }

    @RequestMapping(value = "api/specie/{id}", method = RequestMethod.POST)
    public Specie saveSpecie(@PathVariable String id,
                             @RequestBody @Valid Specie specie) throws CommonObjectNotFound {
        return save(specie, id);
    }

    @RequestMapping(value = "api/specie/{id}", method = RequestMethod.DELETE)
    public Specie deleteSpecie(@PathVariable(required = false) String id) throws CommonObjectNotFound {
        return speciesService.delete(id);
    }

    private Specie save(Specie specie, String id) throws CommonObjectNotFound {
        specie.setId(id);
        return speciesService.save(id, specie);
    }
}
