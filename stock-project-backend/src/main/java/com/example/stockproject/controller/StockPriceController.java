package com.example.stockproject.controller;

import com.example.stockproject.model.dto.StockPriceDTO;
import com.example.stockproject.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 股票歷史價格控制器
 * 
 * 提供股票歷史價格數據的 REST API 端點，包括查詢歷史價格、
 * 按時間範圍查詢、生成模擬數據等功能。
 * 
 * @author Stock Project Team
 * @version 1.1 - 新增歷史股價功能
 */
@RestController
@RequestMapping("/api/stock-prices")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    /**
     * 獲取指定股票的歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @return 歷史價格數據列表
     */
    @GetMapping("/{stockId}")
    public ResponseEntity<List<StockPriceDTO>> getStockPrices(@PathVariable Long stockId) {
        log.info("收到獲取股票 ID: {} 歷史價格數據的請求", stockId);

        try {
            List<StockPriceDTO> stockPrices = stockPriceService.getStockPrices(stockId);
            log.info("成功獲取股票 ID: {} 的歷史價格數據，共 {} 筆記錄", stockId, stockPrices.size());
            return ResponseEntity.ok(stockPrices);
        } catch (Exception e) {
            log.error("獲取股票 ID: {} 歷史價格數據時發生錯誤", stockId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根據時間範圍獲取指定股票的歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @param period  時間範圍 (3M, 1Y, 2Y, 3Y, 5Y)
     * @return 歷史價格數據列表
     */
    @GetMapping("/{stockId}/period/{period}")
    public ResponseEntity<List<StockPriceDTO>> getStockPricesByPeriod(
            @PathVariable Long stockId,
            @PathVariable String period) {
        log.info("收到獲取股票 ID: {} 在時間範圍 {} 的歷史價格數據請求", stockId, period);

        try {
            List<StockPriceDTO> stockPrices = stockPriceService.getStockPricesByPeriod(stockId, period);
            log.info("成功獲取股票 ID: {} 在時間範圍 {} 的歷史價格數據，共 {} 筆記錄",
                    stockId, period, stockPrices.size());
            return ResponseEntity.ok(stockPrices);
        } catch (Exception e) {
            log.error("獲取股票 ID: {} 在時間範圍 {} 的歷史價格數據時發生錯誤", stockId, period, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 為指定股票生成模擬歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @param days    生成的天數 (預設 365 天)
     * @return 操作結果
     */
    @PostMapping("/{stockId}/generate-mock-data")
    public ResponseEntity<String> generateMockData(
            @PathVariable Long stockId,
            @RequestParam(defaultValue = "365") int days) {
        log.info("收到為股票 ID: {} 生成 {} 天模擬歷史價格數據的請求", stockId, days);

        try {
            stockPriceService.generateMockData(stockId, days);
            log.info("成功為股票 ID: {} 生成模擬歷史價格數據", stockId);
            return ResponseEntity.ok("成功生成模擬歷史價格數據");
        } catch (Exception e) {
            log.error("為股票 ID: {} 生成模擬歷史價格數據時發生錯誤", stockId, e);
            return ResponseEntity.internalServerError().body("生成模擬數據失敗");
        }
    }

    /**
     * 檢查指定股票是否有歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @return 是否有歷史數據
     */
    @GetMapping("/{stockId}/has-data")
    public ResponseEntity<Boolean> hasHistoricalData(@PathVariable Long stockId) {
        log.info("檢查股票 ID: {} 是否有歷史價格數據", stockId);

        try {
            boolean hasData = stockPriceService.hasHistoricalData(stockId);
            log.info("股票 ID: {} 是否有歷史價格數據: {}", stockId, hasData);
            return ResponseEntity.ok(hasData);
        } catch (Exception e) {
            log.error("檢查股票 ID: {} 是否有歷史價格數據時發生錯誤", stockId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 刪除指定股票的歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @return 操作結果
     */
    @DeleteMapping("/{stockId}")
    public ResponseEntity<String> deleteHistoricalData(@PathVariable Long stockId) {
        log.info("收到刪除股票 ID: {} 歷史價格數據的請求", stockId);

        try {
            stockPriceService.deleteHistoricalData(stockId);
            log.info("成功刪除股票 ID: {} 的歷史價格數據", stockId);
            return ResponseEntity.ok("成功刪除歷史價格數據");
        } catch (Exception e) {
            log.error("刪除股票 ID: {} 歷史價格數據時發生錯誤", stockId, e);
            return ResponseEntity.internalServerError().body("刪除歷史數據失敗");
        }
    }

    /**
     * 獲取支援的時間範圍列表
     * 
     * @return 支援的時間範圍
     */
    @GetMapping("/supported-periods")
    public ResponseEntity<String[]> getSupportedPeriods() {
        log.info("收到獲取支援時間範圍的請求");

        String[] periods = { "3M", "1Y", "2Y", "3Y", "5Y" };
        return ResponseEntity.ok(periods);
    }

    /**
     * 從 Yahoo Finance 取得真實歷史股價數據
     * 
     * @param symbol 股票代碼
     * @param period 時間範圍 (1d, 5d, 1mo, 3mo, 6mo, 1y, 2y, 5y, 10y, ytd, max)
     * @return 歷史價格數據列表
     */
    @GetMapping("/yahoo/{symbol}/period/{period}")
    public ResponseEntity<List<StockPriceDTO>> getRealStockPricesFromYahoo(
            @PathVariable String symbol,
            @PathVariable String period) {
        log.info("收到從 Yahoo Finance 取得股票 {} 在時間範圍 {} 的真實歷史價格數據請求", symbol, period);

        try {
            List<StockPriceDTO> stockPrices = stockPriceService.getRealStockPricesFromYahoo(symbol, period);
            log.info("成功從 Yahoo Finance 取得股票 {} 的真實歷史價格數據，共 {} 筆記錄",
                    symbol, stockPrices.size());
            return ResponseEntity.ok(stockPrices);
        } catch (Exception e) {
            log.error("從 Yahoo Finance 取得股票 {} 的真實歷史價格數據時發生錯誤", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 從 Alpha Vantage 取得真實歷史股價數據
     * 
     * @param symbol 股票代碼
     * @param apiKey Alpha Vantage API Key
     * @return 歷史價格數據列表
     */
    @GetMapping("/alphavantage/{symbol}")
    public ResponseEntity<List<StockPriceDTO>> getRealStockPricesFromAlphaVantage(
            @PathVariable String symbol,
            @RequestParam String apiKey) {
        log.info("收到從 Alpha Vantage 取得股票 {} 的真實歷史價格數據請求", symbol);

        try {
            List<StockPriceDTO> stockPrices = stockPriceService.getRealStockPricesFromAlphaVantage(symbol, apiKey);
            log.info("成功從 Alpha Vantage 取得股票 {} 的真實歷史價格數據，共 {} 筆記錄",
                    symbol, stockPrices.size());
            return ResponseEntity.ok(stockPrices);
        } catch (Exception e) {
            log.error("從 Alpha Vantage 取得股票 {} 的真實歷史價格數據時發生錯誤", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}