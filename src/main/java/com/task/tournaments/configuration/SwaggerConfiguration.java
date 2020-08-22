package com.task.tournaments.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    private static final String SWAGGER_API_VERSION = "1.0";
    private static final String LICENSE_TEXT = "Licensed by Bogdan Martseniuk";
    private static final String TITLE = "Tournaments rest api";
    private static final String DESCRIPTION = "A web service (HTTP REST API) to work with Tournaments";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .license(LICENSE_TEXT)
                .version(SWAGGER_API_VERSION)
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.task.tournaments"))
                .paths(PathSelectors.regex("/api/v1.*"))
                .build()
                .apiInfo(apiInfo());
    }
}
