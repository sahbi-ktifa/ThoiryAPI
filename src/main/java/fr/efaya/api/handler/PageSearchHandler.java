package fr.efaya.api.handler;

import fr.efaya.api.PictureSearchContext;
import fr.efaya.domain.Picture;
import org.springframework.data.domain.Page;

/**
 * Created by sktifa on 30/11/2016.
 */
public interface PageSearchHandler {
    boolean accept(PictureSearchContext context);
    Page<Picture> resolve(PictureSearchContext context);
}
