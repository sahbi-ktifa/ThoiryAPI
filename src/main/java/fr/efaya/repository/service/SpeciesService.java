package fr.efaya.repository.service;

import fr.efaya.domain.CommonObject;
import fr.efaya.domain.Specie;
import fr.efaya.repository.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Service
public class SpeciesService implements CRUDService {
    private SpeciesRepository repository;

    @Autowired
    public SpeciesService(SpeciesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Specie save(String id, CommonObject object) throws CommonObjectNotFound {
        if (id != null && repository.findOne(id) == null) {
            throw new CommonObjectNotFound();
        }
        return repository.save((Specie)object);
    }

    @Override
    public Specie findById(String id) throws CommonObjectNotFound {
        if (id != null && repository.findOne(id) == null) {
            throw new CommonObjectNotFound();
        }
        return repository.findOne(id);
    }

    @Override
    public List<Specie> findAll() {
        return repository.findAll();
    }

    @Override
    public Specie delete(String id) throws CommonObjectNotFound {
        Specie specie = findById(id);
        repository.delete(specie);
        return specie;
    }
}
