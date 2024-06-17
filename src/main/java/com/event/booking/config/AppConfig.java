package com.event.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.Executor;

@Configuration
@ComponentScan("com.event.booking")
@EnableWebMvc
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
public class AppConfig implements WebMvcConfigurer, AsyncConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedOrigins("http://localhost:3000", "http://localhost:4200/")
                .allowCredentials(true)
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "User-Agent", "Cookie", "Connection");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer config) {
        config.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("asyncExecutor-");
        executor.initialize();
        return executor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}