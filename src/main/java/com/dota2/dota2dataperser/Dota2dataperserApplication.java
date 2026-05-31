package com.dota2.dota2dataperser;

import com.dota2.dota2dataperser.client.OpenDotaClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Dota2dataperserApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dota2dataperserApplication.class, args);
	}
}
