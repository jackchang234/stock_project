package com.example.stockproject.controller;

import com.example.stockproject.model.dto.WatchlistDTO;
import com.example.stockproject.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 觀察清單控制器
 * 
 * 提供觀察清單相關的 REST API 端點，包括查詢、新增和移除功能。
 * 處理 HTTP 請求並返回適當的回應。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private static final String DEFAULT_USER_ID = "guest";

    /**
     * 取得用戶的觀察清單
     * 
     * @return 觀察清單項目列表
     */
    @GetMapping
    public ResponseEntity<List<WatchlistDTO>> getWatchlist() {
        log.info("收到取得觀察清單請求，用戶: {}", DEFAULT_USER_ID);
        List<WatchlistDTO> watchlist = watchlistService.getWatchlist(DEFAULT_USER_ID);
        return ResponseEntity.ok(watchlist);
    }

    /**
     * 新增股票到觀察清單
     * 
     * @param request 包含股票識別碼的請求物件
     * @return 新增的觀察清單項目，如果失敗則返回錯誤訊息
     */
    @PostMapping
    public ResponseEntity<?> addToWatchlist(@RequestBody Map<String, Long> request) {
        Long stockId = request.get("stockId");
        log.info("收到新增股票到觀察清單請求，用戶: {}, 股票: {}", DEFAULT_USER_ID, stockId);

        if (stockId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "股票識別碼不能為空"));
        }

        WatchlistDTO watchlistItem = watchlistService.addToWatchlist(DEFAULT_USER_ID, stockId);
        if (watchlistItem != null) {
            return ResponseEntity.ok(watchlistItem);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "無法新增股票到觀察清單"));
        }
    }

    /**
     * 從觀察清單移除股票
     * 
     * @param stockId 股票識別碼
     * @return 成功移除返回 200，否則返回錯誤訊息
     */
    @DeleteMapping("/{stockId}")
    public ResponseEntity<?> removeFromWatchlist(@PathVariable Long stockId) {
        log.info("收到從觀察清單移除股票請求，用戶: {}, 股票: {}", DEFAULT_USER_ID, stockId);

        boolean removed = watchlistService.removeFromWatchlist(DEFAULT_USER_ID, stockId);
        if (removed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "無法從觀察清單移除股票"));
        }
    }

    /**
     * 檢查股票是否在觀察清單中
     * 
     * @param stockId 股票識別碼
     * @return 如果在觀察清單中返回 true，否則返回 false
     */
    @GetMapping("/check/{stockId}")
    public ResponseEntity<Map<String, Boolean>> checkInWatchlist(@PathVariable Long stockId) {
        log.info("收到檢查股票是否在觀察清單請求，用戶: {}, 股票: {}", DEFAULT_USER_ID, stockId);
        boolean inWatchlist = watchlistService.isInWatchlist(DEFAULT_USER_ID, stockId);
        return ResponseEntity.ok(Map.of("inWatchlist", inWatchlist));
    }
}