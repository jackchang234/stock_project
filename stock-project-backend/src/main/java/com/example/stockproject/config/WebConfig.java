package com.example.stockproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置類別
 * 
 * 配置 Web 相關設定，包括 CORS 政策等。
 * 允許前端應用程式從不同來源存取 API。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置 CORS 政策
     * 
     * 允許前端應用程式（http://localhost:3000）存取後端 API。
     * 支援所有常用的 HTTP 方法。
     * 
     * @param registry CORS 註冊器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}