package fr.efaya.domain;

import java.io.Serializable;

/**
 * Created by sktifa on 25/11/2016.
 */
public abstract class CommonObject implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
