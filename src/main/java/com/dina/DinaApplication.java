package com.dina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the DINA academic planning service.
*/
@SpringBootApplication
public class DinaApplication {

    /**
     * Protected constructor to allow CGLIB proxying.
     */
    protected DinaApplication() {
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(DinaApplication.class, args);
    }
}
