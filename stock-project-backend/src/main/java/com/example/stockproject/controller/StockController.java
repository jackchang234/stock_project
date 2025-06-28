package com.example.stockproject.controller;

import com.example.stockproject.model.dto.StockDTO;
import com.example.stockproject.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 股票控制器
 * 
 * 提供股票相關的 REST API 端點，包括查詢和搜尋功能。
 * 處理 HTTP 請求並返回適當的回應。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {

    private final StockService stockService;

    /**
     * 取得所有股票
     * 
     * @return 所有股票的列表
     */
    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        log.info("收到取得所有股票的請求");
        List<StockDTO> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    /**
     * 根據關鍵字搜尋股票
     * 
     * @param query 搜尋關鍵字
     * @return 符合條件的股票列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<StockDTO>> searchStocks(@RequestParam(required = false) String query) {
        log.info("收到搜尋股票請求，關鍵字: {}", query);
        List<StockDTO> stocks = stockService.searchStocks(query);
        return ResponseEntity.ok(stocks);
    }

    /**
     * 根據股票識別碼取得股票
     * 
     * @param id 股票識別碼
     * @return 股票資訊，如果不存在則返回 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
        log.info("收到取得股票請求，ID: {}", id);
        StockDTO stock = stockService.getStockById(id);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根據股票代碼取得股票
     * 
     * @param symbol 股票代碼
     * @return 股票資訊，如果不存在則返回 404
     */
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<StockDTO> getStockBySymbol(@PathVariable String symbol) {
        log.info("收到根據代碼取得股票請求，代碼: {}", symbol);
        StockDTO stock = stockService.getStockBySymbol(symbol);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}