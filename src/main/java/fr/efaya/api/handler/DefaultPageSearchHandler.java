package fr.efaya.api.handler;

import fr.efaya.Constants;
import fr.efaya.api.PictureSearchContext;
import fr.efaya.domain.Picture;
import fr.efaya.repository.PicturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Created by sktifa on 30/11/2016.
 */
@Component
public class DefaultPageSearchHandler implements PageSearchHandler {
    @Autowired
    private PicturesRepository repository;

    @Override
    public boolean accept(PictureSearchContext context) {
        return context != null && (context.getSpeciesId() == null || context.getSpeciesId().isEmpty());
    }

    @Override
    public Page<Picture> resolve(PictureSearchContext context) {
        return repository.findAll(new PageRequest(context.getPage(), Constants.PAGE,
                new Sort(new Sort.Order(Sort.Direction.DESC, "creationDate"))));
    }
}
