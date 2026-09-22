package com.raju.DevTrack.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI devTrackopenApi(){
        return new OpenAPI().info(
                new Info()
                        .title("Dev Track")
                        .description("REST API for managing learning goals")
                        .version("1.0"))
                        .addSecurityItem(
                                new SecurityRequirement()
                                        .addList("Bearer Authentication")
                        )
                        .components(
                                new Components()
                                        .addSecuritySchemes(
                                                "Bearer Authentication",
                                                new SecurityScheme()
                                                        .name("Authorization")
                                                        .type(SecurityScheme.Type.HTTP)
                                                        .scheme("bearer")
                                                        .bearerFormat("JWT")
                                        ));
    }
}
