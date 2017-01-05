package fr.efaya.api;

import fr.efaya.Constants;
import fr.efaya.domain.Animal;
import fr.efaya.domain.Picture;
import fr.efaya.repository.service.AnimalsService;
import fr.efaya.repository.service.CommonObjectNotFound;
import fr.efaya.repository.service.PicturesService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sktifa on 25/11/2016.
 */
@RestController
@CrossOrigin
public class PictureWebServiceController {

    @Autowired
    private PicturesService picturesService;

    @Autowired
    private AnimalsService animalsService;

    @RequestMapping(value = "common/picture", method = RequestMethod.GET)
    public PictureResultContext retrieveAllPictures(@RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) String specieId,
                                                    @RequestParam(required = false) String animalId) {
        PictureSearchContext pictureSearchContext = new PictureSearchContext(page != null ? page : 0);
        if (specieId != null) {
            pictureSearchContext.setSpeciesId(specieId);
        } else if (animalId != null) {
            pictureSearchContext.setAnimalId(animalId);
        }
        return picturesService.findAll(pictureSearchContext);
    }

    @RequestMapping(value = "api/picture", method = RequestMethod.POST)
    public Picture createPicture(@RequestBody @Valid Picture picture, Principal principal) throws CommonObjectNotFound {
        picture.setCreationDate(new Date());
        picture.setLastModified(new Date());
        picture.setLiked(0);
        picture.setUsername(principal.getName());
        return save(picture, null);
    }

    @RequestMapping(value = "api/picture/{id}", method = RequestMethod.POST)
    public Picture savePicture(@PathVariable String id,
                               @RequestBody @Valid Picture picture) throws CommonObjectNotFound {
        return save(picture, id);
    }

    @RequestMapping(value = "api/picture/{id}/like", method = RequestMethod.POST)
    public Integer likePicture(@PathVariable String id) throws CommonObjectNotFound {
        Picture picture = picturesService.findById(id);
        picture.setLiked(picture.getLiked() + 1);
        picturesService.save(id, picture);
        return picture.getLiked();
    }

    @RequestMapping(value = "common/picture/{id}/preview", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] retrievePicturePreview(@PathVariable String id,
                                         @RequestParam(required = false, defaultValue = Constants.THUMB) String format) throws CommonObjectNotFound {
        return picturesService.retrievePictureBinary(id, format);
    }

    @RequestMapping(value = "api/picture/{id}/binary", method = RequestMethod.POST)
    public Picture savePictureBinary(@PathVariable String id,
                                     @RequestParam MultipartFile file) throws CommonObjectNotFound {
        if (file == null) {
            throw new PictureBinaryNotAcceptable();
        }
        Picture picture = picturesService.findById(id);
        File binary = null;
        try {
            binary = convert(file);
            String mimeType = new MimetypesFileTypeMap().getContentType(binary);
            if (mimeType == null || !mimeType.split("/")[0].equals("image")) {
                throw new PictureBinaryNotAcceptable();
            }
            picturesService.saveBinary(picture, binary);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (binary != null) {
                FileUtils.deleteQuietly(binary);
            }
        }
        return picture;
    }

    @RequestMapping(value = "api/picture/{id}", method = RequestMethod.DELETE)
    public Picture deletePicture(@PathVariable(required = false) String id) throws CommonObjectNotFound {
        return picturesService.delete(id);
    }

    private Picture save(Picture picture, String id) throws CommonObjectNotFound {
        picture.setId(id);
        if (picture.getAnimalIds() != null && !picture.getAnimalIds().isEmpty()) {
            for (String animalId : picture.getAnimalIds()) {
                Animal animal = animalsService.findById(animalId);
                if (picture.getSpeciesIds() == null) {
                    picture.setSpeciesIds(new ArrayList<>());
                }
                if (!picture.getSpeciesIds().contains(animal.getSpecieId())) {
                    picture.getSpeciesIds().add(animal.getSpecieId());
                }
            }
        }
        return picturesService.save(id, picture);
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public class PictureBinaryNotAcceptable extends CommonObjectNotFound {
    }
}
