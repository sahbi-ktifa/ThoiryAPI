package fr.efaya.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;

@Component
public class KeepMeAlive {

    private static boolean initialized;

    @Scheduled(fixedRate=1000000)
    public void keepMeAlive() {
        if (initialized && LocalTime.now().getHour() > 19) {
            return;//Do not keep alive
        }
        initialized = true;
        new RestTemplate().getForObject("https://thoiryphotos-dev.herokuapp.com/common/animal", Object.class);
    }
}
