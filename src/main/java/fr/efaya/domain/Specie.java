package fr.efaya.domain;

import fr.efaya.Constants;

import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
public class Specie extends CommonObject {
    private String name;
    private String description;
    private List<Constants.ORIGIN> origins;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Constants.ORIGIN> getOrigins() {
        return origins;
    }

    public void setOrigins(List<Constants.ORIGIN> origins) {
        this.origins = origins;
    }
}
