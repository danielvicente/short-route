package br.com.bexs.infraestructure.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Slf4j
@Configuration
@Profile("local")
public class CrossOriginConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer(Properties properties) {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST")
                        .allowCredentials(true);
            }
        };
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "http.cors")
    public static class Properties {
        private List<String> allowedOrigins;
    }
}
