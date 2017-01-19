package fr.efaya.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sktifa on 19/01/2017.
 */
public class Animals implements Serializable {
    private List<Animal> animals;

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }
}
