package com.deliverytech.delivery_api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deliveryTechOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DeliveryTech API")
                        .description("Documentação oficial da API do sistema de delivery criado pela Gaby Cavalheiro. " +
                                "Esta API permite gerenciar restaurantes, produtos, pedidos e autenticação via JWT.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gaby Cavalheiro")
                                .email("gaby@example.com")
                                .url("https://github.com/bagista2623"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório oficial no GitHub")
                        .url("https://github.com/bagista2623/delivery-api-gaby"));
    }
}
