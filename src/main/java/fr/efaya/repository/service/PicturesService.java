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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.efaya.Constants.formats;

/**
 * Created by sktifa on 25/11/2016.
 */
@Service
public class PicturesService implements CRUDService {
    private PicturesRepository repository;
    private GridFsOperations gridFsOperations;
    private List<PageSearchHandler> pageSearchHandlers;

    private static double[] BOUNDARY_GEO_LAT_MAX = {48.0, 52.0, 0.0};
    private static double[] BOUNDARY_GEO_LAT_MIN = {48.0, 50.0, 0.0};
    private static double[] BOUNDARY_GEO_LON_MAX = {1.0, 49.0, 0.0};
    private static double[] BOUNDARY_GEO_LON_MIN = {1.0, 45.0, 0.0};

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
            /*Metadata metadata = ImageMetadataReader.readMetadata(file);
            GpsDirectory directory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if (directory == null) {
                delete(picture.getId());
                System.out.println("No geolocation information");
                throw new BadGeolocationException();
            }
            GpsDescriptor descriptor = new GpsDescriptor(directory);
            if (descriptor.getGpsLongitudeDescription() == null
                || descriptor.getGpsLatitudeDescription() == null
                || isLocationUnacceptable(descriptor.getGpsLongitudeDescription(), descriptor.getGpsLatitudeDescription())) {
                delete(picture.getId());
                System.out.println("Incorrect geolocation information");
                throw new BadGeolocationException();
            }*/
        /*Metadata metadata = new Metadata();
        try (FileInputStream inputstream = new FileInputStream(file)) {
            new AutoDetectParser().parse(inputstream, new BodyContentHandler(), metadata, new ParseContext());
        } catch (TikaException | SAXException | IOException e) {

        }
        if (metadata.get("geo:lat") == null || metadata.get("geo:long") == null ||
                isLocationUnacceptable(metadata.get("geo:lat"), metadata.get("geo:long"))) {
            delete(picture.getId());
            throw new BadGeolocationException();
        }*/
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

    private boolean isLocationUnacceptable(String longitude, String latitude) {
        Double lat = Double.valueOf(latitude);
        if (lat < 1.77 || lat > 1.81) {
            return true;
        }
        Double lon = Double.valueOf(longitude);
        if (lon < 48.84 || lon > 48.88) {
            return true;
        }
        return false;
    }

    /*private boolean isLocationUnacceptable(String longitude, String latitude) {
        List<Double> lat = resolveCoord(latitude);
        if (checkCoordOut(lat, BOUNDARY_GEO_LAT_MIN, BOUNDARY_GEO_LAT_MAX)) {
            return true;
        }
        List<Double> lon = resolveCoord(longitude);
        if (checkCoordOut(lon, BOUNDARY_GEO_LON_MIN, BOUNDARY_GEO_LON_MAX)) {
            return true;
        }
        return false;
    }*/

    private boolean checkCoordOut(List<Double> coord, double[] refMin, double[] refMax) {
        if (CollectionUtils.isEmpty(coord) || coord.size() < 3) {
            return true;
        }
        if (coord.get(0) < refMin[0] || coord.get(0) > refMax[0]) {
            return true;
        }
        if (coord.get(1) < refMin[1] || coord.get(1) > refMax[1]) {
            return true;
        }
        return false;
    }

    private List<Double> resolveCoord(String coord) {
        String[] split = coord.split(" ");
        return Stream.of(split)
                .map(d -> Double.valueOf(d.replaceAll("[^\\d.]", "")))
                .collect(Collectors.toList());
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
                    BufferedImage bimg = ImageIO.read(file.getInputStream());
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
