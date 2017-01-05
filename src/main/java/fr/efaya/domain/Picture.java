package fr.efaya.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Document(collection="pictures")
public class Picture extends CommonObject {
    private List<String> animalIds = new ArrayList<>();
    private List<String> speciesIds = new ArrayList<>();
    private String binaryId;
    private String title;
    private Integer liked = 0;
    private Date creationDate;
    private Date lastModified;
    private String username;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
