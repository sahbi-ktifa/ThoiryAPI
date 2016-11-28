package fr.efaya.api;

import java.io.Serializable;

/**
 * Created by sktifa on 28/11/2016.
 */
public class PictureSearchContext implements Serializable {
    private Integer page;

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
}
