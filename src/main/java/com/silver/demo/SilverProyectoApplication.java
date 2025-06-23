package com.silver.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.silver.demo.security.JwtConfig;

@EnableConfigurationProperties(JwtConfig.class)
@SpringBootApplication
public class SilverProyectoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SilverProyectoApplication.class, args);
	}

}
