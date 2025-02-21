package com.example.ggj_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIR = Paths.get("").toAbsolutePath().toString()+ File.separator+"src"+File.separator+"uploads"+File.separator;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/potoUrl/**")
                .addResourceLocations("file:" + UPLOAD_DIR);
    }
}