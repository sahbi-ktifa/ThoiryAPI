package fr.efaya.api;

import fr.efaya.domain.Picture;
import fr.efaya.repository.service.CommonObjectNotFound;
import fr.efaya.repository.service.PicturesService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
@RestController
@RequestMapping("api/picture")
public class PictureWebServiceController {

    @Autowired
    private PicturesService picturesService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Picture> retrieveAllPictures() {
        return picturesService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Picture createPicture(@RequestBody @Valid Picture picture) throws CommonObjectNotFound {
        return save(picture, null);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Picture savePicture(@PathVariable(required = false) String id,
                               @RequestBody @Valid Picture picture) throws CommonObjectNotFound {
        return save(picture, id);
    }

    @RequestMapping(value = "{id}/binary", method = RequestMethod.POST)
    public Picture savePictureBinary(@PathVariable String id,
                                     @RequestBody MultipartFile file) throws CommonObjectNotFound {
        Picture picture = picturesService.findById(id);
        File binary = null;
        try {
            binary = convert(file);
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

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Picture deletePicture(@PathVariable(required = false) String id) throws CommonObjectNotFound {
        return picturesService.delete(id);
    }

    private Picture save(Picture picture, String id) throws CommonObjectNotFound {
        picture.setId(id);
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
}
