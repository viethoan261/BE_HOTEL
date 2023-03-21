package com.example.hotel.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .securityContexts(List.of(getSecurityContext()))
//                .securitySchemes(List.of(getSecurityScheme()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.hotel"))
                .build()
                .apiInfo(new ApiInfoBuilder().title("Hotel Application")
                        .version("1.0.0")
                        .description("This project is used for education purpose only.")
                        .contact(new Contact("123", "123", "123"))
                        .build());

    }

//    private ApiKey getSecurityScheme() {
//
//        return new ApiKey("jwt", "Authorization", "header");
//    }
//
//    private SecurityContext getSecurityContext() {
//
//        return SecurityContext.builder().securityReferences(List.of(getSecurityReferences())).build();
//    }
//
//    private SecurityReference getSecurityReferences() {
//        AuthorizationScope globalScope = new AuthorizationScope("global", "use for all request");
//        AuthorizationScope scopes[] = new AuthorizationScope[1];
//        scopes[0] = globalScope;
//        return new SecurityReference("JWT", scopes);
//
//
//    }
}