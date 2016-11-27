package fr.efaya.repository.service;

import fr.efaya.Constants;
import fr.efaya.domain.Animal;
import fr.efaya.domain.CommonObject;
import fr.efaya.repository.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Service
public class AnimalsService implements CRUDService {

    private AnimalsRepository repository;

    @Autowired
    public AnimalsService(AnimalsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Animal save(String id, CommonObject object) throws CommonObjectNotFound {
        if (id != null && repository.findOne(id) == null) {
            throw new CommonObjectNotFound();
        }
        return repository.save((Animal) object);
    }

    @Override
    public Animal findById(String id) throws CommonObjectNotFound {
        if (id != null && repository.findOne(id) == null) {
            throw new CommonObjectNotFound();
        }
        return repository.findOne(id);
    }

    @Override
    public List<Animal> findAll() {
        return repository.findAll(new PageRequest(0, Constants.PAGE)).getContent();
    }

    @Override
    public Animal delete(String id) throws CommonObjectNotFound {
        Animal animal = findById(id);
        repository.delete(animal);
        return animal;
    }
}
