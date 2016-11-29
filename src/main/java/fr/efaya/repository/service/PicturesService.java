package fr.efaya.repository.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import fr.efaya.Constants;
import fr.efaya.api.PictureResultContext;
import fr.efaya.api.PictureSearchContext;
import fr.efaya.domain.CommonObject;
import fr.efaya.domain.Picture;
import fr.efaya.repository.PicturesRepository;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
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
        Picture picture = (Picture) object;
        picture.setLastModified(new Date());
        return repository.save(picture);
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

    public byte[] retrievePictureBinary(String id, String format) throws CommonObjectNotFound {
        Picture picture = findById(id);
        if (picture.getBinaryId() != null) {
            GridFSDBFile file = gridFsOperations.findOne(new Query(Criteria.where("_id").is(picture.getBinaryId())));
            if (file != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    Constants.Format resolvedFormat = Constants.formats.get(format) != null ? Constants.formats.get(format) : Constants.formats.get(Constants.THUMB);
                    BufferedImage resizedImage = new BufferedImage(resolvedFormat.getWidth(), resolvedFormat.getHeight(), ColorSpace.TYPE_RGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(ImageIO.read(file.getInputStream()), 0, 0, resolvedFormat.getWidth(), resolvedFormat.getHeight(), null);
                    g.dispose();

                    ImageIO.write(resizedImage, "jpg", baos);
                    baos.flush();
                    byte[] result = baos.toByteArray();
                    baos.close();
                    return result;
                } catch (IOException e) {
                    throw new PictureBinaryNotFound();
                }
            }

        }
        throw new PictureBinaryNotFound();
    }

    private class PictureBinaryNotFound extends CommonObjectNotFound {
    }
}
