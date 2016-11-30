package fr.efaya.repository.service;

import fr.efaya.domain.Animal;
import fr.efaya.domain.CommonObject;
import fr.efaya.repository.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return repository.findAll();
    }

    @Override
    public Animal delete(String id) throws CommonObjectNotFound {
        Animal animal = findById(id);
        repository.delete(animal);
        return animal;
    }

    public List<Animal> retrieveSiblings(String id) throws CommonObjectNotFound {
        Animal animal = findById(id);
        List<Animal> animals = repository.findBySpecieId(animal.getSpecieId());
        return animals.stream().filter(a -> !a.getId().equals(id)).collect(Collectors.toList());
    }
}
