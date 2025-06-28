# 股票市場初學者專案

## 專案概述

這是一個專為股票市場初學者設計的 Web 應用程式，允許用戶查看股票資訊、管理觀察清單，並追蹤基本股票數據（如價格、名稱、代碼）。**版本 1.1 新增了歷史股價線圖功能**，提供更豐富的股票分析體驗。

### 技術架構

- **後端**: Spring Boot 3.x (Java) 搭配 Spring Web、Spring Data JPA 和 H2 記憶體資料庫
- **前端**: React 18.x 搭配 TypeScript，使用 Axios 進行 API 呼叫，Tailwind CSS 進行樣式設計，Chart.js 繪製圖表
- **API**: RESTful API 連接後端和前端

### 核心功能

- 顯示股票列表（代碼、名稱、當前價格）
- 允許用戶新增/移除觀察清單中的股票
- 基本搜尋功能，可依代碼或名稱搜尋股票
- **歷史股價線圖顯示（3 個月、1 年、2 年、3 年、5 年）**
- **股票詳情頁面，包含完整的 OHLCV 數據**
- **模擬歷史數據生成功能**
- 簡潔、響應式的用戶介面

### 未來擴展性

程式碼庫採用模組化設計且文件完整，可輕鬆新增功能如用戶認證、即時股票數據或投資組合追蹤。

## 功能特色

- ✅ 股票列表顯示
- ✅ 觀察清單管理
- ✅ 股票搜尋功能
- ✅ **歷史股價線圖**
- ✅ **時間範圍選擇（3M、1Y、2Y、3Y、5Y）**
- ✅ **股票詳情頁面**
- ✅ **模擬數據生成**
- ✅ 響應式設計
- ✅ RESTful API
- ✅ 模組化架構

## 環境需求

### 必要工具

- **Java 17** 或更高版本
- **Node.js 18** 或更高版本
- **Maven 3.6** 或更高版本
- **npm** 或 **yarn**

### 檢查版本

```bash
java -version
node --version
mvn --version
npm --version
```

## 設置說明

### 後端設置

1. **克隆專案**

   ```bash
   git clone <repository-url>
   cd stock-project-backend
   ```

2. **建置專案**

   ```bash
   mvn clean install
   ```

3. **啟動伺服器**

   ```bash
   mvn spring-boot:run
   ```

4. **驗證啟動**
   - 後端服務運行在: http://localhost:8080
   - H2 資料庫控制台: http://localhost:8080/h2-console
   - 資料庫憑證: 請查看 `application.properties`

### 前端設置

1. **進入前端目錄**

   ```bash
   cd stock-project-frontend
   ```

2. **安裝依賴**

   ```bash
   npm install
   ```

3. **啟動開發伺服器**

   ```bash
   npm run dev
   ```

4. **驗證啟動**
   - 前端應用運行在: http://localhost:3000

## 使用指南

### 基本操作

1. **瀏覽股票列表**

   - 訪問首頁查看所有股票
   - 使用搜尋欄搜尋特定股票

2. **管理觀察清單**

   - 點擊「新增到觀察清單」按鈕
   - 在觀察清單頁面查看已新增的股票
   - 點擊 X 按鈕移除股票

3. **查看股票詳情**
   - 點擊股票列表中的任意行
   - 或點擊觀察清單中的股票卡片
   - 查看歷史價格線圖和統計數據

### 歷史股價功能

1. **生成模擬數據**

   - 首次訪問股票詳情頁面時，系統會提示生成模擬數據
   - 點擊「生成模擬數據」按鈕
   - 等待數據生成完成

2. **查看歷史線圖**
   - 選擇時間範圍：3 個月、1 年、2 年、3 年、5 年
   - 查看收盤價走勢圖
   - 瀏覽數據統計（最高價、最低價、平均成交量等）

## API 文件

### 股票相關端點

#### 取得所有股票

```
GET /api/stocks
```

**回應範例:**

```json
[
  {
    "id": 1,
    "symbol": "AAPL",
    "name": "Apple Inc.",
    "price": 150.25
  }
]
```

#### 搜尋股票

```
GET /api/stocks/search?query={query}
```

**參數:**

- `query`: 搜尋關鍵字（股票代碼或名稱，不區分大小寫）

**回應範例:**

```json
[
  {
    "id": 1,
    "symbol": "AAPL",
    "name": "Apple Inc.",
    "price": 150.25
  }
]
```

### 歷史價格相關端點

#### 取得股票歷史價格

```
GET /api/stock-prices/{stockId}
```

**參數:**

- `stockId`: 股票識別碼

**回應範例:**

```json
[
  {
    "id": 1,
    "stockId": 1,
    "symbol": "AAPL",
    "date": "2024-01-15",
    "openPrice": 150.0,
    "closePrice": 151.25,
    "highPrice": 152.5,
    "lowPrice": 149.75,
    "volume": 500000
  }
]
```

#### 根據時間範圍取得歷史價格

```
GET /api/stock-prices/{stockId}/period/{period}
```

**參數:**

- `stockId`: 股票識別碼
- `period`: 時間範圍 (3M, 1Y, 2Y, 3Y, 5Y)

#### 生成模擬歷史數據

```
POST /api/stock-prices/{stockId}/generate-mock-data?days={days}
```

**參數:**

- `stockId`: 股票識別碼
- `days`: 生成的天數（預設 365）

#### 檢查是否有歷史數據

```
GET /api/stock-prices/{stockId}/has-data
```

#### 取得支援的時間範圍

```
GET /api/stock-prices/supported-periods
```

### 觀察清單相關端點

#### 取得觀察清單

```
GET /api/watchlist
```

**回應範例:**

```json
[
  {
    "id": 1,
    "stockId": 1,
    "stock": {
      "id": 1,
      "symbol": "AAPL",
      "name": "Apple Inc.",
      "price": 150.25
    }
  }
]
```

#### 新增股票到觀察清單

```
POST /api/watchlist
```

**請求內容:**

```json
{
  "stockId": 1
}
```

#### 從觀察清單移除股票

```
DELETE /api/watchlist/{stockId}
```

**參數:**

- `stockId`: 要移除的股票 ID

## 目錄結構

### 後端結構

```
stock-project-backend/
├── src/main/java/com/example/stockproject/
│   ├── config/
│   │   └── WebConfig.java
│   ├── controller/
│   │   ├── StockController.java
│   │   ├── StockPriceController.java    # 新增：歷史價格控制器
│   │   └── WatchlistController.java
│   ├── model/
│   │   ├── dto/
│   │   │   ├── StockDTO.java
│   │   │   ├── StockPriceDTO.java       # 新增：歷史價格 DTO
│   │   │   └── WatchlistDTO.java
│   │   └── entity/
│   │       ├── Stock.java
│   │       ├── StockPrice.java          # 新增：歷史價格實體
│   │       └── Watchlist.java
│   ├── repository/
│   │   ├── StockRepository.java
│   │   ├── StockPriceRepository.java    # 新增：歷史價格儲存庫
│   │   └── WatchlistRepository.java
│   ├── service/
│   │   ├── StockService.java
│   │   ├── StockPriceService.java       # 新增：歷史價格服務
│   │   └── WatchlistService.java
│   └── StockProjectApplication.java
└── src/main/resources/
    ├── application.properties
    └── data.sql
```

### 前端結構

```
stock-project-frontend/
├── src/
│   ├── components/
│   │   ├── SearchBar.tsx
│   │   ├── StockList.tsx                # 更新：新增點擊跳轉功能
│   │   └── Watchlist.tsx                # 更新：新增點擊跳轉功能
│   ├── pages/
│   │   ├── Home.tsx
│   │   ├── StockDetailPage.tsx          # 新增：股票詳情頁面
│   │   └── WatchlistPage.tsx
│   ├── services/
│   │   └── api.ts                       # 更新：新增歷史價格 API
│   ├── types/
│   │   └── index.ts                     # 更新：新增歷史價格類型
│   ├── App.tsx                          # 更新：新增路由配置
│   └── main.tsx
├── package.json                         # 更新：新增 Chart.js 依賴
└── ...
```

## 版本更新記錄

### 版本 1.1 - 歷史股價功能 (2024-12-19)

#### 新增功能

- ✅ 股票歷史價格實體和資料庫表
- ✅ 歷史價格 API 端點（查詢、按時間範圍查詢、生成模擬數據）
- ✅ 股票詳情頁面，包含歷史價格線圖
- ✅ 時間範圍選擇器（3 個月、1 年、2 年、3 年、5 年）
- ✅ Chart.js 整合，繪製響應式線圖
- ✅ 模擬歷史數據生成功能
- ✅ 點擊股票列表和觀察清單跳轉到詳情頁面
- ✅ 數據統計顯示（最高價、最低價、平均成交量等）

#### 技術改進

- 新增 `StockPrice` 實體類別，支援完整的 OHLCV 數據
- 實現模擬數據生成算法，包含價格波動和成交量模擬
- 前端整合 Chart.js 和 react-chartjs-2 繪製專業圖表
- 優化用戶體驗，新增載入狀態和錯誤處理
- 完整的 TypeScript 類型定義

#### 修改記錄

- 後端：新增 4 個檔案，修改 1 個檔案
- 前端：新增 1 個檔案，修改 5 個檔案
- 資料庫：更新初始化腳本註解

### 版本 1.0 - 基礎功能 (2024-12-18)

#### 初始功能

- ✅ 股票列表顯示
- ✅ 觀察清單管理
- ✅ 股票搜尋功能
- ✅ 響應式設計
- ✅ RESTful API
- ✅ 模組化架構

## 開發指南

### 新增功能

1. **後端開發**

   - 在 `model/entity` 中定義實體類別
   - 在 `model/dto` 中定義資料傳輸物件
   - 在 `repository` 中定義資料庫操作
   - 在 `service` 中實現業務邏輯
   - 在 `controller` 中定義 API 端點

2. **前端開發**
   - 在 `types` 中定義 TypeScript 類型
   - 在 `services/api.ts` 中新增 API 方法
   - 在 `components` 中建立可重用元件
   - 在 `pages` 中建立頁面元件
   - 在 `App.tsx` 中配置路由

### 測試

```bash
# 後端測試
cd stock-project-backend
mvn test

# 前端測試
cd stock-project-frontend
npm test
```

### 建置

```bash
# 後端建置
cd stock-project-backend
mvn clean package

# 前端建置
cd stock-project-frontend
npm run build
```

## 授權

本專案採用 MIT 授權條款。詳見 [LICENSE](LICENSE) 檔案。

## 貢獻

歡迎提交 Issue 和 Pull Request！

## 聯絡資訊

如有任何問題或建議，請透過以下方式聯絡：

- 建立 GitHub Issue
- 發送 Email

---

**注意**: 這是一個學習專案，所有股票數據均為模擬資料，僅供學習和演示使用。
