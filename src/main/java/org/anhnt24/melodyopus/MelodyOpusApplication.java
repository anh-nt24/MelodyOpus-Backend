package org.anhnt24.melodyopus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MelodyOpusApplication {

	public static void main(String[] args) {
		SpringApplication.run(MelodyOpusApplication.class, args);
		System.out.println("http://localhost:8080/");
	}

}
