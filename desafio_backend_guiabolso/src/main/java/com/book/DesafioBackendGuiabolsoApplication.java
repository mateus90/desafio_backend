package com.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.book.init.BookInit;

@SpringBootApplication
public class DesafioBackendGuiabolsoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DesafioBackendGuiabolsoApplication.class, args);
		BookInit.getInstance().populateDatabase();
	}
}
