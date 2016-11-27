package fr.efaya.api.handling;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sktifa on 27/11/2016.
 */
public class ErrorMessage implements Serializable {
    private String message;
    private Date date;

    public ErrorMessage(String msg, Date date) {
        this.message = msg;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}