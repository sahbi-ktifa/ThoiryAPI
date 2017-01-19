package fr.efaya.domain;

import fr.efaya.Constants;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Document(collection="species")
public class Specie extends CommonObject {
    @NotNull
    private String name;
    private String link;
    private String description;
    private List<Constants.ORIGIN> origins;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
