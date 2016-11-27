package fr.efaya.api;

import fr.efaya.domain.Specie;
import fr.efaya.repository.service.CommonObjectNotFound;
import fr.efaya.repository.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@RestController
@RequestMapping("api/specie")
public class SpecieWebServiceController {

    @Autowired
    private SpeciesService speciesService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Specie> retrieveAllSpecies() {
        return speciesService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Specie createSpecie(@RequestBody @Valid Specie specie) throws CommonObjectNotFound {
        return save(specie, null);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Specie saveSpecie(@PathVariable(required = false) String id,
                             @RequestBody @Valid Specie specie) throws CommonObjectNotFound {
        return save(specie, id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Specie deleteSpecie(@PathVariable(required = false) String id) throws CommonObjectNotFound {
        return speciesService.delete(id);
    }

    private Specie save(Specie specie, String id) throws CommonObjectNotFound {
        specie.setId(id);
        return speciesService.save(id, specie);
    }
}
