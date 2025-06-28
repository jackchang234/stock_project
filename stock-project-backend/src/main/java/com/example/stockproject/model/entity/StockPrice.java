package com.example.stockproject.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 股票歷史價格實體類別
 * 
 * 用於儲存股票的歷史價格數據，支援不同時間範圍的股價追蹤。
 * 每個記錄包含特定日期的開盤價、收盤價、最高價、最低價和成交量。
 * 
 * @author Stock Project Team
 * @version 1.1 - 新增歷史股價功能
 */
@Entity
@Table(name = "stock_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice {

    /**
     * 歷史價格記錄唯一識別碼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 關聯的股票
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    /**
     * 交易日期
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * 開盤價
     */
    @Column(nullable = false)
    private Double openPrice;

    /**
     * 收盤價
     */
    @Column(nullable = false)
    private Double closePrice;

    /**
     * 最高價
     */
    @Column(nullable = false)
    private Double highPrice;

    /**
     * 最低價
     */
    @Column(nullable = false)
    private Double lowPrice;

    /**
     * 成交量
     */
    @Column(nullable = false)
    private Long volume;

    /**
     * 建構函數
     * 
     * @param stock      股票
     * @param date       日期
     * @param openPrice  開盤價
     * @param closePrice 收盤價
     * @param highPrice  最高價
     * @param lowPrice   最低價
     * @param volume     成交量
     */
    public StockPrice(Stock stock, LocalDate date, Double openPrice, Double closePrice,
            Double highPrice, Double lowPrice, Long volume) {
        this.stock = stock;
        this.date = date;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
    }
}