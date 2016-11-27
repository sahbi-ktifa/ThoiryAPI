package fr.efaya.repository.service;

import com.mongodb.gridfs.GridFSFile;
import fr.efaya.domain.CommonObject;
import fr.efaya.domain.Picture;
import fr.efaya.repository.PicturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.UUID;

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
            GridFSFile stored = gridFsOperations.store(inputStream, UUID.randomUUID());
            picture.setBinaryId((String) stored.getId());
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

    @Override
    public Picture delete(String id) throws CommonObjectNotFound {
        Picture picture = findById(id);
        if (picture.getBinaryId() != null) {
            gridFsOperations.delete(new Query(Criteria.where(picture.getBinaryId())));
        }
        repository.delete(picture);
        return picture;
    }
}
