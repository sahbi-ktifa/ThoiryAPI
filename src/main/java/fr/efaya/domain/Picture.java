package fr.efaya.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Document(collection="pictures")
public class Picture extends CommonObject {
    private List<String> animalIds;
    private List<String> speciesIds;
    @NotNull
    private String binaryId;
    private String title;
    private Integer liked;

    public List<String> getAnimalIds() {
        return animalIds;
    }

    public void setAnimalIds(List<String> animalIds) {
        this.animalIds = animalIds;
    }

    public List<String> getSpeciesIds() {
        return speciesIds;
    }

    public void setSpeciesIds(List<String> speciesIds) {
        this.speciesIds = speciesIds;
    }

    public String getBinaryId() {
        return binaryId;
    }

    public void setBinaryId(String binaryId) {
        this.binaryId = binaryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }
}
