package org.plano;

import org.plano.master.PlanoMaster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class PlanoApplication {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.scan("org.plano");
		PlanoMaster planoMaster = (PlanoMaster) applicationContext.getBean("PlanoMaster");
		planoMaster.start();

		SpringApplication.run(PlanoApplication.class, args);
	}
}
