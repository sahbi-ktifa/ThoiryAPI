package fr.efaya.domain;

import java.io.File;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
public class Picture extends CommonObject {
    private List<String> targetIds;
    private File picture;
    private String title;
    private Integer liked;

    public List<String> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<String> targetIds) {
        this.targetIds = targetIds;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
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
