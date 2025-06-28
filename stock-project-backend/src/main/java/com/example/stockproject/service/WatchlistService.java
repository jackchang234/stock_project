package com.example.stockproject.service;

import com.example.stockproject.model.dto.StockDTO;
import com.example.stockproject.model.dto.WatchlistDTO;
import com.example.stockproject.model.entity.Stock;
import com.example.stockproject.model.entity.Watchlist;
import com.example.stockproject.repository.StockRepository;
import com.example.stockproject.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 觀察清單服務類別
 * 
 * 提供觀察清單相關的業務邏輯，包括新增、移除、查詢等功能。
 * 負責在控制器和儲存庫之間進行資料轉換和業務處理。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final StockRepository stockRepository;

    /**
     * 取得用戶的觀察清單
     * 
     * @param userId 用戶識別碼
     * @return 觀察清單 DTO 列表
     */
    public List<WatchlistDTO> getWatchlist(String userId) {
        log.info("取得用戶觀察清單: {}", userId);
        List<Watchlist> watchlist = watchlistRepository.findByUserIdWithStock(userId);
        return watchlist.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 新增股票到觀察清單
     * 
     * @param userId  用戶識別碼
     * @param stockId 股票識別碼
     * @return 新增的觀察清單項目 DTO，如果失敗則返回 null
     */
    public WatchlistDTO addToWatchlist(String userId, Long stockId) {
        log.info("新增股票到觀察清單 - 用戶: {}, 股票: {}", userId, stockId);

        // 檢查股票是否存在
        if (!stockRepository.existsById(stockId)) {
            log.warn("股票不存在: {}", stockId);
            return null;
        }

        // 檢查是否已在觀察清單中
        if (watchlistRepository.existsByUserIdAndStockId(userId, stockId)) {
            log.warn("股票已在觀察清單中 - 用戶: {}, 股票: {}", userId, stockId);
            return null;
        }

        // 取得股票並新增到觀察清單
        Stock stock = stockRepository.findById(stockId).orElse(null);
        if (stock != null) {
            Watchlist watchlistItem = new Watchlist(userId, stock);
            Watchlist saved = watchlistRepository.save(watchlistItem);
            log.info("成功新增股票到觀察清單: {}", stockId);
            return convertToDTO(saved);
        }

        return null;
    }

    /**
     * 從觀察清單移除股票
     * 
     * @param userId  用戶識別碼
     * @param stockId 股票識別碼
     * @return 如果成功移除返回 true，否則返回 false
     */
    public boolean removeFromWatchlist(String userId, Long stockId) {
        log.info("從觀察清單移除股票 - 用戶: {}, 股票: {}", userId, stockId);

        if (!watchlistRepository.existsByUserIdAndStockId(userId, stockId)) {
            log.warn("觀察清單項目不存在 - 用戶: {}, 股票: {}", userId, stockId);
            return false;
        }

        watchlistRepository.deleteByUserIdAndStockId(userId, stockId);
        log.info("成功從觀察清單移除股票: {}", stockId);
        return true;
    }

    /**
     * 檢查股票是否在觀察清單中
     * 
     * @param userId  用戶識別碼
     * @param stockId 股票識別碼
     * @return 如果在觀察清單中返回 true，否則返回 false
     */
    public boolean isInWatchlist(String userId, Long stockId) {
        return watchlistRepository.existsByUserIdAndStockId(userId, stockId);
    }

    /**
     * 將觀察清單實體轉換為 DTO
     * 
     * @param watchlist 觀察清單實體
     * @return 觀察清單 DTO
     */
    private WatchlistDTO convertToDTO(Watchlist watchlist) {
        StockDTO stockDTO = new StockDTO(
                watchlist.getStock().getId(),
                watchlist.getStock().getSymbol(),
                watchlist.getStock().getName(),
                watchlist.getStock().getPrice());

        return new WatchlistDTO(
                watchlist.getId(),
                watchlist.getStock().getId(),
                stockDTO);
    }
}