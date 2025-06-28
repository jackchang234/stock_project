package com.example.stockproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 股票市場專案主應用程式類別
 * 
 * 這是 Spring Boot 應用程式的入口點，負責啟動整個應用程式。
 * 使用 @SpringBootApplication 註解來啟用自動配置和元件掃描。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@SpringBootApplication
public class StockProjectApplication {

    /**
     * 應用程式主方法
     * 
     * @param args 命令列參數
     */
    public static void main(String[] args) {
        SpringApplication.run(StockProjectApplication.class, args);
        System.out.println("🚀 股票市場專案已啟動!");
        System.out.println("📊 後端 API: http://localhost:8080");
        System.out.println("🗄️  H2 資料庫控制台: http://localhost:8080/h2-console");
        System.out.println("📈 健康檢查: http://localhost:8080/actuator/health");
    }
} 