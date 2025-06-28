package com.example.stockproject.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 股票實體類別
 * 
 * 代表股票市場中的一支股票，包含股票的基本資訊如代碼、名稱和價格。
 * 使用 JPA 註解進行資料庫映射。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    /**
     * 股票唯一識別碼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 股票代碼（如 AAPL、GOOGL）
     */
    @Column(unique = true, nullable = false, length = 10)
    private String symbol;

    /**
     * 股票名稱（如 Apple Inc.）
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 當前股票價格
     */
    @Column(nullable = false)
    private Double price;

    /**
     * 建構函數
     * 
     * @param symbol 股票代碼
     * @param name   股票名稱
     * @param price  股票價格
     */
    public Stock(String symbol, String name, Double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}