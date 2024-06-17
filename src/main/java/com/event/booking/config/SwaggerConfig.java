package com.event.booking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static com.event.booking.util.ConfigParams.*;
import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    @Value("${app.api.auth.url}")
    private String API_AUTHENTICATION_URL;

    private boolean enableSwagger = true;

    @Bean
    public Docket api() {
        List<ResponseMessage> list = new java.util.ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enableSwagger)
                //.select()
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                //.pathMapping("/")
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, list)
                .globalResponseMessage(RequestMethod.PATCH, list)
                .globalResponseMessage(RequestMethod.OPTIONS, list)
                .globalResponseMessage(RequestMethod.PUT, list)
                .globalResponseMessage(RequestMethod.DELETE, list)
                .globalResponseMessage(RequestMethod.POST, list);
    }


    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private OAuth securitySchema() {
        List<AuthorizationScope> authorizationScopeList = newArrayList();
        authorizationScopeList.add(new AuthorizationScope("read", "read all"));
        authorizationScopeList.add(new AuthorizationScope("trust", "trust all"));
        authorizationScopeList.add(new AuthorizationScope("write", "access all"));
        List<GrantType> grantTypes = newArrayList();
        GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant(API_AUTHENTICATION_URL);
        grantTypes.add(creGrant);
        return new OAuth(APP_NAME, authorizationScopeList, grantTypes);
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("Authorization", authorizationScopes));
    }


  /*  private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }*/

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

/*    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
        authorizationScopes[0] = new AuthorizationScope("read", "read all");
        authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
        authorizationScopes[2] = new AuthorizationScope("write", "write all");
        return Collections.singletonList(new SecurityReference("oauth2schema", authorizationScopes));
    }*/

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(CLIENT_ID, CLIENT_SECRET, "", APP_NAME, "", ApiKeyVehicle.HEADER, "", " ");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .termsOfServiceUrl(WEBSITE_URL)
                .contact(new Contact(CONTACT_NAME, WEBSITE_URL, CONTACT_EMAIL))
                .license(LICENSE)
                .licenseUrl(WEBSITE_URL)
                .version(VERSION)
                .build();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
