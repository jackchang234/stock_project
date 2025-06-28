package com.example.stockproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 股票歷史價格資料傳輸物件 (DTO)
 * 
 * 用於 API 回應中傳輸股票歷史價格資料，包含完整的 OHLCV 數據。
 * 支援前端圖表顯示和數據分析。
 * 
 * @author Stock Project Team
 * @version 1.1 - 新增歷史股價功能
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceDTO {

    /**
     * 歷史價格記錄唯一識別碼
     */
    private Long id;

    /**
     * 股票識別碼
     */
    private Long stockId;

    /**
     * 股票代碼
     */
    private String symbol;

    /**
     * 交易日期
     */
    private LocalDate date;

    /**
     * 開盤價 (Open)
     */
    private Double openPrice;

    /**
     * 收盤價 (Close)
     */
    private Double closePrice;

    /**
     * 最高價 (High)
     */
    private Double highPrice;

    /**
     * 最低價 (Low)
     */
    private Double lowPrice;

    /**
     * 成交量 (Volume)
     */
    private Long volume;
}