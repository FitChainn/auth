package auth.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Auth API - FitChain")
                        .description("API DE AUTENTICACIÓN Y GESTIÓN DE TOKENS JWT")
                        .version("1.0.0")).servers(List.of(
                        new Server().url("http://localhost:8092").description("Local")
                ));
    }
}