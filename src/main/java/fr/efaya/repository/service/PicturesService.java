package fr.efaya.repository.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import fr.efaya.Constants;
import fr.efaya.api.PictureResultContext;
import fr.efaya.api.PictureSearchContext;
import fr.efaya.api.handler.PageSearchHandler;
import fr.efaya.domain.CommonObject;
import fr.efaya.domain.Picture;
import fr.efaya.repository.PicturesRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static fr.efaya.Constants.formats;

/**
 * Created by sktifa on 25/11/2016.
 */
@Service
public class PicturesService implements CRUDService {
    private PicturesRepository repository;
    private GridFsOperations gridFsOperations;
    private List<PageSearchHandler> pageSearchHandlers;

    @Autowired
    public PicturesService(PicturesRepository repository, GridFsOperations gridFsOperations, List<PageSearchHandler> handlers) {
        this.repository = repository;
        this.gridFsOperations = gridFsOperations;
        this.pageSearchHandlers = handlers;
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

    public void saveBinary(Picture picture, File file) throws CommonObjectNotFound, BadGeolocationException {
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            GridFSFile stored = gridFsOperations.store(inputStream, file.getName());
            picture.setBinaryId(stored.getId().toString());
            save(picture.getId(), picture);
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
        return repository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "creationDate")));
    }

    public PictureResultContext findAll(PictureSearchContext pictureSearchContext) {
        if (pageSearchHandlers != null) {
            Optional<PageSearchHandler> handlerOptional = pageSearchHandlers.stream().filter(h -> h.accept(pictureSearchContext)).findFirst();
            if (handlerOptional.isPresent()) {
                final Page<Picture> page = handlerOptional.get().resolve(pictureSearchContext);
                PictureResultContext pictureResultContext = new PictureResultContext();
                pictureResultContext.setPage(pictureSearchContext.getPage());
                pictureResultContext.setTotal((int) page.getTotalElements());
                pictureResultContext.setPictures(page.getContent());
                return pictureResultContext;
            }
        }
        return null;
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
        return retrievePictureBinary(id, Constants.THUMB);
    }

    public byte[] retrievePictureBinary(String id, String format) throws CommonObjectNotFound {
        Picture picture = findById(id);
        if (picture.getBinaryId() != null) {
            GridFSDBFile file = retrieveImageBinary(picture);
            if (file != null) {
                Constants.Format boundary = formats.get(format) != null ? formats.get(format) : formats.get(Constants.THUMB);
                try {
                    //BufferedImage bimg = ImageIO.read(file.getInputStream());
                    BufferedImage bimg = Thumbnails.of(file.getInputStream()).scale(1).asBufferedImage();
                    Constants.Format aimedFormat = Constants.getScaledDimension(new Constants.Format(bimg.getWidth(), bimg.getHeight()), boundary);
                    return getImageAsByteArray(bimg, aimedFormat.getWidth(), aimedFormat.getHeight());
                } catch (IOException e) {
                    delete(id);
                    throw new PictureBinaryNotFound();
                }
            }
        }
        delete(id);
        throw new PictureBinaryNotFound();
    }

    private GridFSDBFile retrieveImageBinary(Picture picture) {
        return gridFsOperations.findOne(new Query(Criteria.where("_id").is(picture.getBinaryId())));
    }

    private byte[] getImageAsByteArray(BufferedImage bimg, int width, int height) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage resizedImage = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bimg, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        ImageIO.write(resizedImage, "png", baos);
        baos.flush();
        byte[] result = baos.toByteArray();
        baos.close();
        return result;
    }

    public Picture findOneBySpeciesId(String specieId) {
        return repository.findOneBySpeciesIdsIn(Collections.singletonList(specieId), new Sort(
                new Sort.Order(Sort.Direction.DESC, "liked"),
                new Sort.Order(Sort.Direction.DESC, "creationDate")
        ));
    }

    public Picture findOneByAnimalsId(String animalId) {
        return repository.findOneByAnimalIdsIn(Collections.singletonList(animalId), new Sort(
                new Sort.Order(Sort.Direction.DESC, "liked"),
                new Sort.Order(Sort.Direction.DESC, "creationDate")
        ));
    }

    private class PictureBinaryNotFound extends CommonObjectNotFound {
    }
}
