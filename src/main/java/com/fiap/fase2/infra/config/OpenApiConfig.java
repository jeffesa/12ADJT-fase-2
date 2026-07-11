package com.fiap.fase2.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String API_TITLE = "API de Gestão de Restaurantes";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = """
            API RESTful para gestão de restaurantes desenvolvida como Tech Challenge Fase 2 da FIAP.

            **Funcionalidades:**
            - Cadastro de tipos de usuário
            - Cadastro de usuários (donos de restaurante e clientes)
            - Cadastro de restaurantes
            - Cadastro de itens do cardápio
            - Autenticação via login e senha
            - Troca de senha

            **Arquitetura:** Clean Architecture (Domain, Application, Infrastructure)

            **Tipos de usuário:** Gerenciados via CRUD (ex: CUSTOMER, RESTAURANT_OWNER)
            """;
    private static final String CONTACT_NAME = "FIAP Tech Challenge - Fase 2";
    private static final String CONTACT_URL = "https://github.com/jeffesa/12ADJT-fase-2";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION)
                        .contact(new Contact()
                                .name(CONTACT_NAME)
                                .url(CONTACT_URL)))
                .tags(List.of(
                        new Tag().name("Tipos de Usuário").description("CRUD de tipos de usuário"),
                        new Tag().name("Usuários").description("Cadastro, consulta, atualização e exclusão de usuários"),
                        new Tag().name("Autenticação").description("Login e troca de senha"),
                        new Tag().name("Restaurantes").description("CRUD de restaurantes"),
                        new Tag().name("Cardápio").description("CRUD de itens do cardápio")
                ));
    }
}
