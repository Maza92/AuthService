package com.auth.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Spring Autentication API", version = "1.0", description = "API for Spring Authentication"), servers = {
        @Server(url = "/api/v1", description = "Default Server URL")
})
public class SwaggerConfig {
}
