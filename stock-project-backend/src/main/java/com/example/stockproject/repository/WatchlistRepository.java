package com.example.stockproject.repository;

import com.example.stockproject.model.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 觀察清單儲存庫介面
 * 
 * 提供觀察清單資料的資料庫操作功能，包括基本的 CRUD 操作和自定義查詢。
 * 繼承 JpaRepository 以獲得基本的資料庫操作方法。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    /**
     * 根據用戶識別碼查詢觀察清單
     * 
     * @param userId 用戶識別碼
     * @return 該用戶的觀察清單列表
     */
    @Query("SELECT w FROM Watchlist w JOIN FETCH w.stock WHERE w.userId = :userId")
    List<Watchlist> findByUserIdWithStock(@Param("userId") String userId);

    /**
     * 根據用戶識別碼和股票識別碼查詢觀察清單項目
     * 
     * @param userId  用戶識別碼
     * @param stockId 股票識別碼
     * @return 觀察清單項目，如果不存在則返回空
     */
    Optional<Watchlist> findByUserIdAndStockId(String userId, Long stockId);

    /**
     * 檢查用戶是否已將特定股票加入觀察清單
     * 
     * @param userId  用戶識別碼
     * @param stockId 股票識別碼
     * @return 如果已存在返回 true，否則返回 false
     */
    boolean existsByUserIdAndStockId(String userId, Long stockId);

    /**
     * 根據用戶識別碼和股票識別碼刪除觀察清單項目
     * 
     * @param userId  用戶識別碼
     * @param stockId 股票識別碼
     */
    void deleteByUserIdAndStockId(String userId, Long stockId);
}