package com.example.stockproject.service;

import com.example.stockproject.model.dto.StockDTO;
import com.example.stockproject.model.entity.Stock;
import com.example.stockproject.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 股票服務類別
 * 
 * 提供股票相關的業務邏輯，包括查詢、搜尋等功能。
 * 負責在控制器和儲存庫之間進行資料轉換和業務處理。
 * 
 * @author Stock Project Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    /**
     * 取得所有股票
     * 
     * @return 所有股票的 DTO 列表
     */
    public List<StockDTO> getAllStocks() {
        log.info("取得所有股票");
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根據關鍵字搜尋股票
     * 
     * @param query 搜尋關鍵字
     * @return 符合條件的股票 DTO 列表
     */
    public List<StockDTO> searchStocks(String query) {
        log.info("搜尋股票，關鍵字: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return getAllStocks();
        }

        List<Stock> stocks = stockRepository.findBySymbolOrNameContainingIgnoreCase(query.trim());
        return stocks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根據股票識別碼取得股票
     * 
     * @param id 股票識別碼
     * @return 股票 DTO，如果不存在則返回 null
     */
    public StockDTO getStockById(Long id) {
        log.info("根據 ID 取得股票: {}", id);
        return stockRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * 根據股票代碼取得股票
     * 
     * @param symbol 股票代碼
     * @return 股票 DTO，如果不存在則返回 null
     */
    public StockDTO getStockBySymbol(String symbol) {
        log.info("根據代碼取得股票: {}", symbol);
        Stock stock = stockRepository.findBySymbol(symbol);
        return stock != null ? convertToDTO(stock) : null;
    }

    /**
     * 檢查股票是否存在
     * 
     * @param id 股票識別碼
     * @return 如果存在返回 true，否則返回 false
     */
    public boolean stockExists(Long id) {
        return stockRepository.existsById(id);
    }

    /**
     * 將股票實體轉換為 DTO
     * 
     * @param stock 股票實體
     * @return 股票 DTO
     */
    private StockDTO convertToDTO(Stock stock) {
        return new StockDTO(
                stock.getId(),
                stock.getSymbol(),
                stock.getName(),
                stock.getPrice());
    }
}