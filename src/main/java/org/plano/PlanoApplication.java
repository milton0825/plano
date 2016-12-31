package org.plano;

import org.plano.master.PlanoMaster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Entry point of Plano.
 */
@SpringBootApplication
public class PlanoApplication {
    /**
     * Scan and register all the beans and initialize {@link PlanoMaster} to start scheduling jobs.
     * @param args {@link String[]}
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.scan("org.plano");
        PlanoMaster planoMaster = (PlanoMaster) applicationContext.getBean("PlanoMaster");
        planoMaster.start();

        SpringApplication.run(PlanoApplication.class, args);
    }
}
