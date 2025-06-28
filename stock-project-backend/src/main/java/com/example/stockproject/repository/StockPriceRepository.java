package com.example.stockproject.repository;

import com.example.stockproject.model.entity.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 股票歷史價格儲存庫介面
 * 
 * 提供股票歷史價格資料的資料庫操作功能，包括按時間範圍查詢、
 * 按股票查詢等。支援不同時間範圍的歷史數據檢索。
 * 
 * @author Stock Project Team
 * @version 1.1 - 新增歷史股價功能
 */
@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

        /**
         * 根據股票識別碼查詢歷史價格數據
         * 
         * @param stockId 股票識別碼
         * @return 該股票的歷史價格列表
         */
        List<StockPrice> findByStockIdOrderByDateAsc(Long stockId);

        /**
         * 根據股票識別碼和時間範圍查詢歷史價格數據
         * 
         * @param stockId   股票識別碼
         * @param startDate 開始日期
         * @param endDate   結束日期
         * @return 指定時間範圍內的歷史價格列表
         */
        @Query("SELECT sp FROM StockPrice sp WHERE sp.stock.id = :stockId " +
                        "AND sp.date >= :startDate AND sp.date <= :endDate " +
                        "ORDER BY sp.date ASC")
        List<StockPrice> findByStockIdAndDateRange(
                        @Param("stockId") Long stockId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        /**
         * 根據股票識別碼查詢最近的歷史價格數據
         * 
         * @param stockId 股票識別碼
         * @return 最近的歷史價格列表
         */
        @Query("SELECT sp FROM StockPrice sp WHERE sp.stock.id = :stockId " +
                        "ORDER BY sp.date DESC")
        List<StockPrice> findRecentByStockId(@Param("stockId") Long stockId);

        /**
         * 檢查股票是否有歷史價格數據
         * 
         * @param stockId 股票識別碼
         * @return 如果有數據返回 true，否則返回 false
         */
        boolean existsByStockId(Long stockId);

        /**
         * 根據股票識別碼刪除所有歷史價格數據
         * 
         * @param stockId 股票識別碼
         */
        void deleteByStockId(Long stockId);
}