package fr.efaya.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by sktifa on 25/11/2016.
 */
@Document(collection="animals")
public class Animal extends CommonObject {
    @NotNull
    private String specieId;
    @NotNull
    private String name;
    @Size(max = 4, min = 4)
    private String birthYear;

    public String getSpecieId() {
        return specieId;
    }

    public void setSpecieId(String specieId) {
        this.specieId = specieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
}
