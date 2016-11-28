package fr.efaya.api.handling;

import fr.efaya.api.PictureWebServiceController;
import fr.efaya.repository.service.CommonObjectNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

/**
 * Created by sktifa on 27/11/2016.
 */
@RestControllerAdvice
public class ApiWebServiceAdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(CommonObjectNotFound.class)
    public ErrorMessage handleNotFound() {
        return new ErrorMessage("Entity not found", new Date());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 404
    @ExceptionHandler(PictureWebServiceController.PictureBinaryNotAcceptable.class)
    public ErrorMessage handleNotAcceptable() {
        return new ErrorMessage("Media is not acceptable", new Date());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)  // 404
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorMessage handleAccessDenied() {
        return new ErrorMessage("You are not allowed to execute this request", new Date());
    }
}
