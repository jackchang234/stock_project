package com.example.stockproject.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 觀察清單實體類別
 * 
 * 代表用戶的股票觀察清單，記錄用戶關注的股票。
 * 目前使用預設用戶 "guest"，未來可擴展為多用戶系統。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Entity
@Table(name = "watchlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Watchlist {

    /**
     * 觀察清單項目唯一識別碼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用戶識別碼（目前預設為 "guest"）
     */
    @Column(nullable = false, length = 50)
    private String userId = "guest";

    /**
     * 關聯的股票
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    /**
     * 建構函數
     * 
     * @param userId 用戶識別碼
     * @param stock  股票物件
     */
    public Watchlist(String userId, Stock stock) {
        this.userId = userId;
        this.stock = stock;
    }

    /**
     * 建構函數（使用預設用戶）
     * 
     * @param stock 股票物件
     */
    public Watchlist(Stock stock) {
        this.userId = "guest";
        this.stock = stock;
    }
}