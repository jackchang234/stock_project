package com.example.stockproject.repository;

import com.example.stockproject.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 股票儲存庫介面
 * 
 * 提供股票資料的資料庫操作功能，包括基本的 CRUD 操作和自定義查詢。
 * 繼承 JpaRepository 以獲得基本的資料庫操作方法。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     * 根據股票代碼或名稱搜尋股票（不區分大小寫）
     * 
     * @param query 搜尋關鍵字
     * @return 符合條件的股票列表
     */
    @Query("SELECT s FROM Stock s WHERE LOWER(s.symbol) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Stock> findBySymbolOrNameContainingIgnoreCase(@Param("query") String query);

    /**
     * 根據股票代碼查詢股票
     * 
     * @param symbol 股票代碼
     * @return 股票物件，如果不存在則返回 null
     */
    Stock findBySymbol(String symbol);

    /**
     * 檢查股票代碼是否存在
     * 
     * @param symbol 股票代碼
     * @return 如果存在返回 true，否則返回 false
     */
    boolean existsBySymbol(String symbol);
}