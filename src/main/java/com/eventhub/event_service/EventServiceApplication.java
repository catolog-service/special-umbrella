package com.eventhub.event_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class EventServiceApplication {

	public static void main(String[] args) {
		log.info("🚀 Iniciando Event Service Application...");
		SpringApplication.run(EventServiceApplication.class, args);
		log.info("✅ Event Service Application iniciado com sucesso!");
	}

}
