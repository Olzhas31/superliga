package com.example.superligasparta.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
        .info(new Info()
            .title("Super Liga API")
            .description("API для управления футбольными командами")
            .version("v1.0")
            .contact(new Contact()
                .name("Olzhas Suleimenov")
                .email("you@example.com")
            )
        );
  }

//  @Bean
//  public GroupedOpenApi teamsApi() {
//    return GroupedOpenApi.builder()
//        .group("teams")
//        .pathsToMatch("/teams/**")
//        .build();
//  }

}

