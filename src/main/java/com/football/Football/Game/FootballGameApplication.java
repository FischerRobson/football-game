package com.football.Football.Game;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FootballGameApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
		System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
		System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
		SpringApplication.run(FootballGameApplication.class, args);
		System.out.println("" +
				"\n" +
				"______          _   _           _ _   _____                      \n" +
				"|  ___|        | | | |         | | | |  __ \\                     \n" +
				"| |_ ___   ___ | |_| |__   __ _| | | | |  \\/ __ _ _ __ ___   ___ \n" +
				"|  _/ _ \\ / _ \\| __| '_ \\ / _` | | | | | __ / _` | '_ ` _ \\ / _ \\\n" +
				"| || (_) | (_) | |_| |_) | (_| | | | | |_\\ \\ (_| | | | | | |  __/\n" +
				"\\_| \\___/ \\___/ \\__|_.__/ \\__,_|_|_|  \\____/\\__,_|_| |_| |_|\\___|\n" +
				"                                                                 \n" +
				"                                                                 \n");
	}

}
