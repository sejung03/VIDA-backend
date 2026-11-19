package com.camorix.camorixspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CamorixSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamorixSpringApplication.class, args);
    }

}
