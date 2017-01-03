package fr.efaya.api;

import java.io.Serializable;

/**
 * Created by sktifa on 28/11/2016.
 */
public class PictureSearchContext implements Serializable {
    private Integer page;
    private String speciesId;
    private String animalId;

    public PictureSearchContext() {
    }

    public PictureSearchContext(int page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(String speciesId) {
        this.speciesId = speciesId;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }
}
