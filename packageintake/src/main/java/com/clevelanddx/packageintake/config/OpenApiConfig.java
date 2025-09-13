package com.clevelanddx.packageintake.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI packageIntakeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Package Intake API")
                        .description("API for managing package intake operations")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Cleveland DX")
                                .email("support@clevelanddx.com")));
    }
} 