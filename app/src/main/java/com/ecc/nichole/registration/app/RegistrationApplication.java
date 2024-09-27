package com.ecc.nichole.registration.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.ecc.nichole.registration.core",
		"com.ecc.nichole.registration.infra",
		"com.ecc.nichole.registration.app"
})
public class RegistrationApplication {
	public static void main(String[] args) {
		SpringApplication.run(RegistrationApplication.class, args);
	}
}