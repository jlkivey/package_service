package com.clevelanddx.packageintake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})
@EnableWebMvc
@ComponentScan(basePackages = "com.clevelanddx.packageintake")
public class PackageintakeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PackageintakeApplication.class, args);
	}

}
