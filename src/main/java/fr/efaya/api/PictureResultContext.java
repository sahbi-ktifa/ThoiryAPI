package fr.efaya.api;

import fr.efaya.domain.Picture;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sktifa on 28/11/2016.
 */
public class PictureResultContext implements Serializable {
    private Integer page;
    private Integer total;
    private List<Picture> pictures;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
