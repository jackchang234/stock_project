package com.example.stockproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 股票資料傳輸物件 (DTO)
 * 
 * 用於 API 回應中傳輸股票資料，避免直接暴露實體類別。
 * 提供股票的基本資訊給前端使用。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    /**
     * 股票唯一識別碼
     */
    private Long id;

    /**
     * 股票代碼
     */
    private String symbol;

    /**
     * 股票名稱
     */
    private String name;

    /**
     * 股票價格
     */
    private Double price;
}