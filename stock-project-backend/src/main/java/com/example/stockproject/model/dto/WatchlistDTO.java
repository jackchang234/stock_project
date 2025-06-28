package com.example.stockproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 觀察清單資料傳輸物件 (DTO)
 * 
 * 用於 API 回應中傳輸觀察清單資料，包含股票資訊。
 * 提供觀察清單項目的完整資訊給前端使用。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistDTO {

    /**
     * 觀察清單項目唯一識別碼
     */
    private Long id;

    /**
     * 股票識別碼
     */
    private Long stockId;

    /**
     * 關聯的股票資訊
     */
    private StockDTO stock;
}