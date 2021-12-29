package cz.cvut.fit.tjv.polakemi.semestral_work.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(WebApplication.class);
        app.setLogStartupInfo(false);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

}
