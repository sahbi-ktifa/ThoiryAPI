package fr.efaya.repository.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import fr.efaya.Constants;
import fr.efaya.api.PictureResultContext;
import fr.efaya.api.PictureSearchContext;
import fr.efaya.domain.CommonObject;
import fr.efaya.domain.Picture;
import fr.efaya.repository.PicturesRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@Service
public class PicturesService implements CRUDService {
    private PicturesRepository repository;
    private GridFsOperations gridFsOperations;

    @Autowired
    public PicturesService(PicturesRepository repository, GridFsOperations gridFsOperations) {
        this.repository = repository;
        this.gridFsOperations = gridFsOperations;
    }

    @Override
    public Picture save(String id, CommonObject object) throws CommonObjectNotFound {
        if (id != null && repository.findOne(id) == null) {
            throw new CommonObjectNotFound();
        }
        return repository.save((Picture)object);
    }

    public void saveBinary(Picture picture, File file) throws CommonObjectNotFound {
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            GridFSFile stored = gridFsOperations.store(inputStream, file.getName());
            picture.setBinaryId(stored.getId().toString());
            save(picture.getId(), picture);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Picture findById(String id) throws CommonObjectNotFound {
        if (id != null && repository.findOne(id) == null) {
            throw new CommonObjectNotFound();
        }
        return repository.findOne(id);
    }

    @Override
    public List<Picture> findAll() {
        return repository.findAll();
    }

    public PictureResultContext findAll(PictureSearchContext pictureSearchContext) {
        final Page<Picture> page = repository.findAll(new PageRequest(pictureSearchContext.getPage(), Constants.PAGE));

        PictureResultContext pictureResultContext = new PictureResultContext();
        pictureResultContext.setPage(pictureSearchContext.getPage());
        pictureResultContext.setTotal((int) page.getTotalElements());
        pictureResultContext.setPictures(page.getContent());
        return pictureResultContext;
    }

    @Override
    public Picture delete(String id) throws CommonObjectNotFound {
        Picture picture = findById(id);
        if (picture.getBinaryId() != null) {
            gridFsOperations.delete(new Query(Criteria.where("_id").is(picture.getBinaryId())));
        }
        repository.delete(picture);
        return picture;
    }

    public byte[] retrievePictureBinary(String id) throws CommonObjectNotFound {
        Picture picture = findById(id);
        if (picture.getBinaryId() != null) {
            GridFSDBFile file = gridFsOperations.findOne(new Query(Criteria.where("_id").is(picture.getBinaryId())));
            try {
                return IOUtils.toByteArray(file.getInputStream());
            } catch (IOException e) {
                throw new PictureBinaryNotFound();
            }

        }
        throw new PictureBinaryNotFound();
    }

    private class PictureBinaryNotFound extends CommonObjectNotFound {
    }
}
