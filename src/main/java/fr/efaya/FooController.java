package fr.efaya;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sktifa on 25/11/2016.
 */
@RestController
public class FooController {
    @RequestMapping(method = RequestMethod.GET, value = "**")
    public String hello() {
        return "hello";
    }
}
