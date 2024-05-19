package de.marco1223.strawberry.api;

import net.dv8tion.jda.internal.utils.JDALogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class BootApplication {

    public static void start(String[] args) {
        JDALogger.getLog(BootApplication.class).info("-".repeat(54));
        JDALogger.getLog(BootApplication.class).info("Starting Spring Boot Application");
        JDALogger.getLog(BootApplication.class).info("-".repeat(54));
        SpringApplication app = new SpringApplication(BootApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "3501"));
        app.run(args);
    }

}
