package com.example.stockproject.service;

import com.example.stockproject.model.dto.StockPriceDTO;
import com.example.stockproject.model.entity.Stock;
import com.example.stockproject.model.entity.StockPrice;
import com.example.stockproject.repository.StockPriceRepository;
import com.example.stockproject.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Iterator;

/**
 * 股票歷史價格服務類別
 * 
 * 提供股票歷史價格數據的業務邏輯處理，包括數據查詢、轉換、
 * 時間範圍計算等功能。支援不同時間範圍的歷史數據檢索。
 * 
 * @author Stock Project Team
 * @version 1.1 - 新增歷史股價功能
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockPriceService {

    private final StockPriceRepository stockPriceRepository;
    private final StockRepository stockRepository;

    /**
     * 根據股票識別碼獲取歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @return 歷史價格 DTO 列表
     */
    public List<StockPriceDTO> getStockPrices(Long stockId) {
        log.info("獲取股票 ID: {} 的歷史價格數據", stockId);

        List<StockPrice> stockPrices = stockPriceRepository.findByStockIdOrderByDateAsc(stockId);

        return stockPrices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根據股票識別碼和時間範圍獲取歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @param period  時間範圍 (3M, 1Y, 2Y, 3Y, 5Y)
     * @return 歷史價格 DTO 列表
     */
    public List<StockPriceDTO> getStockPricesByPeriod(Long stockId, String period) {
        log.info("獲取股票 ID: {} 在時間範圍 {} 的歷史價格數據", stockId, period);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(period);

        List<StockPrice> stockPrices = stockPriceRepository.findByStockIdAndDateRange(
                stockId, startDate, endDate);

        return stockPrices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 生成模擬歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @param days    生成的天數
     */
    public void generateMockData(Long stockId, int days) {
        log.info("為股票 ID: {} 生成 {} 天的模擬歷史價格數據", stockId, days);

        Optional<Stock> stockOpt = stockRepository.findById(stockId);
        if (stockOpt.isEmpty()) {
            log.warn("股票 ID: {} 不存在，無法生成歷史價格數據", stockId);
            return;
        }

        Stock stock = stockOpt.get();
        double basePrice = stock.getPrice();
        LocalDate startDate = LocalDate.now().minusDays(days);

        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);

            // 跳過週末
            if (date.getDayOfWeek().getValue() > 5) {
                continue;
            }

            // 生成模擬的 OHLCV 數據
            double volatility = 0.02; // 2% 波動率
            double randomChange = (Math.random() - 0.5) * volatility;
            double closePrice = basePrice * (1 + randomChange);

            double openPrice = closePrice * (1 + (Math.random() - 0.5) * 0.01);
            double highPrice = Math.max(openPrice, closePrice) * (1 + Math.random() * 0.005);
            double lowPrice = Math.min(openPrice, closePrice) * (1 - Math.random() * 0.005);
            long volume = (long) (Math.random() * 1000000) + 100000;

            StockPrice stockPrice = new StockPrice(
                    stock, date, openPrice, closePrice, highPrice, lowPrice, volume);

            stockPriceRepository.save(stockPrice);
            basePrice = closePrice; // 更新基準價格
        }

        log.info("成功為股票 ID: {} 生成模擬歷史價格數據", stockId);
    }

    /**
     * 將實體轉換為 DTO
     * 
     * @param stockPrice 股票價格實體
     * @return 股票價格 DTO
     */
    private StockPriceDTO convertToDTO(StockPrice stockPrice) {
        return new StockPriceDTO(
                stockPrice.getId(),
                stockPrice.getStock().getId(),
                stockPrice.getStock().getSymbol(),
                stockPrice.getDate(),
                stockPrice.getOpenPrice(),
                stockPrice.getClosePrice(),
                stockPrice.getHighPrice(),
                stockPrice.getLowPrice(),
                stockPrice.getVolume());
    }

    /**
     * 根據時間範圍計算開始日期
     * 
     * @param period 時間範圍 (3M, 1Y, 2Y, 3Y, 5Y)
     * @return 開始日期
     */
    private LocalDate calculateStartDate(String period) {
        LocalDate now = LocalDate.now();

        return switch (period.toUpperCase()) {
            case "3M" -> now.minusMonths(3);
            case "1Y" -> now.minusYears(1);
            case "2Y" -> now.minusYears(2);
            case "3Y" -> now.minusYears(3);
            case "5Y" -> now.minusYears(5);
            default -> now.minusMonths(1); // 預設為 1 個月
        };
    }

    /**
     * 檢查股票是否有歷史價格數據
     * 
     * @param stockId 股票識別碼
     * @return 如果有數據返回 true，否則返回 false
     */
    public boolean hasHistoricalData(Long stockId) {
        return stockPriceRepository.existsByStockId(stockId);
    }

    /**
     * 刪除股票的歷史價格數據
     * 
     * @param stockId 股票識別碼
     */
    public void deleteHistoricalData(Long stockId) {
        log.info("刪除股票 ID: {} 的歷史價格數據", stockId);
        stockPriceRepository.deleteByStockId(stockId);
    }

    /**
     * 從 Yahoo Finance 取得真實歷史股價數據
     * 
     * @param symbol 股票代碼
     * @param period 時間範圍 (1d, 5d, 1mo, 3mo, 6mo, 1y, 2y, 5y, 10y, ytd, max)
     * @return 歷史價格 DTO 列表
     */
    public List<StockPriceDTO> getRealStockPricesFromYahoo(String symbol, String period) {
        log.info("從 Yahoo Finance 取得股票 {} 在時間範圍 {} 的真實歷史價格數據", symbol, period);

        try {
            // Yahoo Finance API URL
            String url = String.format(
                    "https://query1.finance.yahoo.com/v8/finance/chart/%s?interval=1d&range=%s",
                    symbol, period);

            // 使用 RestTemplate 呼叫 Yahoo Finance API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseYahooFinanceResponse(response.getBody(), symbol);
            } else {
                log.error("Yahoo Finance API 回應異常: {}", response.getStatusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("從 Yahoo Finance 取得歷史價格數據時發生錯誤", e);
            return new ArrayList<>();
        }
    }

    /**
     * 解析 Yahoo Finance API 回應
     * 
     * @param jsonResponse Yahoo Finance JSON 回應
     * @param symbol       股票代碼
     * @return 歷史價格 DTO 列表
     */
    private List<StockPriceDTO> parseYahooFinanceResponse(String jsonResponse, String symbol) {
        List<StockPriceDTO> stockPrices = new ArrayList<>();

        try {
            // 使用 Jackson 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            JsonNode chart = root.path("chart");
            if (chart.isMissingNode()) {
                log.error("Yahoo Finance 回應格式異常: 找不到 chart 節點");
                return stockPrices;
            }

            JsonNode result = chart.path("result").get(0);
            if (result == null || result.isMissingNode()) {
                log.error("Yahoo Finance 回應格式異常: 找不到 result 節點");
                return stockPrices;
            }

            JsonNode timestamp = result.path("timestamp");
            JsonNode indicators = result.path("indicators");
            JsonNode quote = indicators.path("quote").get(0);

            if (timestamp.isMissingNode() || quote.isMissingNode()) {
                log.error("Yahoo Finance 回應格式異常: 找不到 timestamp 或 quote 節點");
                return stockPrices;
            }

            JsonNode open = quote.path("open");
            JsonNode high = quote.path("high");
            JsonNode low = quote.path("low");
            JsonNode close = quote.path("close");
            JsonNode volume = quote.path("volume");

            for (int i = 0; i < timestamp.size(); i++) {
                try {
                    // 檢查是否有有效數據
                    if (i < open.size() && i < high.size() && i < low.size() &&
                            i < close.size() && i < volume.size() &&
                            !open.get(i).isNull() && !high.get(i).isNull() &&
                            !low.get(i).isNull() && !close.get(i).isNull() &&
                            !volume.get(i).isNull()) {

                        // 轉換時間戳為 LocalDate
                        long timestampValue = timestamp.get(i).asLong();
                        LocalDate date = LocalDate.ofEpochDay(timestampValue / 86400);

                        StockPriceDTO stockPrice = new StockPriceDTO();
                        stockPrice.setId((long) i);
                        stockPrice.setStockId(0L); // 暫時設為 0，因為是外部數據
                        stockPrice.setSymbol(symbol);
                        stockPrice.setDate(date);
                        stockPrice.setOpenPrice(open.get(i).asDouble());
                        stockPrice.setHighPrice(high.get(i).asDouble());
                        stockPrice.setLowPrice(low.get(i).asDouble());
                        stockPrice.setClosePrice(close.get(i).asDouble());
                        stockPrice.setVolume(volume.get(i).asLong());

                        stockPrices.add(stockPrice);
                    }
                } catch (Exception e) {
                    log.warn("解析第 {} 筆數據時發生錯誤: {}", i, e.getMessage());
                }
            }

            log.info("成功從 Yahoo Finance 解析 {} 筆歷史價格數據", stockPrices.size());

        } catch (Exception e) {
            log.error("解析 Yahoo Finance 回應時發生錯誤", e);
        }

        return stockPrices;
    }

    /**
     * 從 Alpha Vantage 取得真實歷史股價數據
     * 
     * @param symbol 股票代碼
     * @param apiKey Alpha Vantage API Key
     * @return 歷史價格 DTO 列表
     */
    public List<StockPriceDTO> getRealStockPricesFromAlphaVantage(String symbol, String apiKey) {
        log.info("從 Alpha Vantage 取得股票 {} 的真實歷史價格數據", symbol);

        try {
            // Alpha Vantage API URL (每日時間序列)
            String url = String.format(
                    "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s",
                    symbol, apiKey);

            // 使用 RestTemplate 呼叫 Alpha Vantage API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseAlphaVantageResponse(response.getBody(), symbol);
            } else {
                log.error("Alpha Vantage API 回應異常: {}", response.getStatusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("從 Alpha Vantage 取得歷史價格數據時發生錯誤", e);
            return new ArrayList<>();
        }
    }

    /**
     * 解析 Alpha Vantage API 回應
     * 
     * @param jsonResponse Alpha Vantage JSON 回應
     * @param symbol       股票代碼
     * @return 歷史價格 DTO 列表
     */
    private List<StockPriceDTO> parseAlphaVantageResponse(String jsonResponse, String symbol) {
        List<StockPriceDTO> stockPrices = new ArrayList<>();

        try {
            // 使用 Jackson 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            // 檢查是否有錯誤訊息
            if (root.has("Error Message")) {
                log.error("Alpha Vantage API 錯誤: {}", root.get("Error Message").asText());
                return stockPrices;
            }

            // 檢查 API 呼叫限制
            if (root.has("Note")) {
                log.warn("Alpha Vantage API 限制: {}", root.get("Note").asText());
                return stockPrices;
            }

            JsonNode timeSeriesDaily = root.path("Time Series (Daily)");
            if (timeSeriesDaily.isMissingNode()) {
                log.error("Alpha Vantage 回應格式異常: 找不到 Time Series (Daily) 節點");
                return stockPrices;
            }

            int count = 0;
            Iterator<String> fieldNames = timeSeriesDaily.fieldNames();
            while (fieldNames.hasNext() && count < 100) {
                try {
                    // 取得日期和價格數據
                    String dateStr = fieldNames.next();
                    JsonNode priceData = timeSeriesDaily.get(dateStr);

                    // 解析日期
                    LocalDate date = LocalDate.parse(dateStr);

                    StockPriceDTO stockPrice = new StockPriceDTO();
                    stockPrice.setId((long) count);
                    stockPrice.setStockId(0L);
                    stockPrice.setSymbol(symbol);
                    stockPrice.setDate(date);
                    stockPrice.setOpenPrice(Double.parseDouble(priceData.get("1. open").asText()));
                    stockPrice.setHighPrice(Double.parseDouble(priceData.get("2. high").asText()));
                    stockPrice.setLowPrice(Double.parseDouble(priceData.get("3. low").asText()));
                    stockPrice.setClosePrice(Double.parseDouble(priceData.get("4. close").asText()));
                    stockPrice.setVolume(Long.parseLong(priceData.get("5. volume").asText()));

                    stockPrices.add(stockPrice);
                    count++;

                } catch (Exception e) {
                    log.warn("解析第 {} 筆數據時發生錯誤: {}", count, e.getMessage());
                }
            }

            log.info("成功從 Alpha Vantage 解析 {} 筆歷史價格數據", stockPrices.size());

        } catch (Exception e) {
            log.error("解析 Alpha Vantage 回應時發生錯誤", e);
        }

        return stockPrices;
    }
}