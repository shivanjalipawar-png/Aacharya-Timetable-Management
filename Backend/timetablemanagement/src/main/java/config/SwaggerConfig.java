package com.aacharya.timetablemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI timetableManagementAPI() {
        return new OpenAPI() .info(new Info()
                .title("Timetable Management System API")
                .description("REST APIs for managing batches, teachers and subjects.")
                .version("1.0")
                .contact(new Contact()
                        .name(":Aacharya Academy")
                        .email("support1729@aacharya.com")));

    }
}
