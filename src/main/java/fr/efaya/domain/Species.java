package fr.efaya.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sktifa on 19/01/2017.
 */
public class Species implements Serializable {
    private List<Specie> species;

    public List<Specie> getSpecies() {
        return species;
    }

    public void setSpecies(List<Specie> species) {
        this.species = species;
    }
}
