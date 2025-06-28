# 股票市場初學者專案 - 更改記錄

## [1.0.0] - 2024-12-19

### 新增功能

- **股票線圖功能**: 整合 Chart.js 繪製歷史股價線圖
- **時間範圍選擇器**: 支援 1 個月、2 個月、3 個月、全部數據
- **股票詳情頁面**: 顯示股票詳細資訊與歷史價格圖表
- **Alpha Vantage API 整合**: 使用真實股票數據 API
- **觀察清單管理**: 新增、刪除、查看觀察清單功能

### 技術架構

- **後端**: Spring Boot 3.x + H2 資料庫
- **前端**: React 18.x + TypeScript + Tailwind CSS
- **API**: Alpha Vantage 股票數據 API
- **圖表**: Chart.js 繪製線圖

### 主要功能

- 股票列表顯示與搜尋
- 股票詳情頁面與歷史價格圖表
- 觀察清單管理
- 響應式設計與現代化 UI

### 啟動方式

```bash
# 後端 (8080端口)
cd stock-project-backend
mvn spring-boot:run

# 前端 (3003端口)
cd stock-project-frontend
npm start
```

### 支援股票代碼

- AAPL, MSFT, GOOGL, TSLA, META, NVDA, AMZN, NFLX, AMD, INTC

### 已知限制

- Alpha Vantage API 有請求頻率限制
- 部分股票代碼可能無數據
- 建議使用主要科技股進行測試

---

_此專案為股票市場初學者學習用途，使用模擬數據與公開 API_
