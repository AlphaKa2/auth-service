package com.alphaka.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${gateway.url}")
    private String baseUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        // AccessToken Security Scheme 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Security Requirement 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("AccessToken");

        return new OpenAPI()
                .info(new Info().title("인증 서비스"))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("AccessToken", securityScheme)
                .addTagsItem(new Tag().name("External API").description("프론트엔드에서 사용하는 외부 API"))
                .addTagsItem(new Tag().name("Internal API").description("내부 서비스 간 호출에 사용하는 API"));
    }

    @Bean
    public OpenApiCustomizer customOpenApiCustomiser() {
        return openApi -> {
            Server gatewayServer = new Server();
            gatewayServer.setUrl(baseUrl + "/auth-service");
            gatewayServer.setDescription("게이트웨이 일반 접근");

            Server gatewayServerWithAuth = new Server();
            gatewayServerWithAuth.setUrl(baseUrl + "/auth-service/auth");
            gatewayServerWithAuth.setDescription("게이트웨이 인증 접근");

            openApi.setServers(List.of(gatewayServer, gatewayServerWithAuth));
        };
    }


}
