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
    "closePrice": 152.5,
    "highPrice": 153.0,
    "lowPrice": 149.5,
    "volume": 1000000
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
- `days`: 生成的天數 (預設 365)

### 觀察清單相關端點

#### 取得觀察清單

```
GET /api/watchlist
```

#### 新增股票到觀察清單

```
POST /api/watchlist
```

**請求體:**

```json
{
  "stockId": 1
}
```

#### 從觀察清單移除股票

```
DELETE /api/watchlist/{stockId}
```

## 資料庫結構

### 股票表 (stocks)

| 欄位   | 類型    | 說明     |
| ------ | ------- | -------- |
| id     | BIGINT  | 主鍵     |
| symbol | VARCHAR | 股票代碼 |
| name   | VARCHAR | 股票名稱 |
| price  | DOUBLE  | 當前價格 |

### 股票價格表 (stock_prices)

| 欄位        | 類型   | 說明     |
| ----------- | ------ | -------- |
| id          | BIGINT | 主鍵     |
| stock_id    | BIGINT | 股票外鍵 |
| date        | DATE   | 日期     |
| open_price  | DOUBLE | 開盤價   |
| close_price | DOUBLE | 收盤價   |
| high_price  | DOUBLE | 最高價   |
| low_price   | DOUBLE | 最低價   |
| volume      | BIGINT | 成交量   |

### 觀察清單表 (watchlist)

| 欄位     | 類型      | 說明     |
| -------- | --------- | -------- |
| id       | BIGINT    | 主鍵     |
| stock_id | BIGINT    | 股票外鍵 |
| added_at | TIMESTAMP | 新增時間 |

## 開發指南

### 專案結構

```
stock-project/
├── stock-project-backend/          # 後端專案
│   ├── src/main/java/
│   │   └── com/example/stockproject/
│   │       ├── controller/         # 控制器層
│   │       ├── service/           # 服務層
│   │       ├── repository/        # 資料存取層
│   │       ├── model/             # 資料模型
│   │       └── config/            # 配置類別
│   ├── src/main/resources/        # 配置檔案
│   └── pom.xml                    # Maven 配置
├── stock-project-frontend/         # 前端專案
│   ├── src/
│   │   ├── components/            # React 元件
│   │   ├── pages/                 # 頁面元件
│   │   ├── services/              # API 服務
│   │   └── types/                 # TypeScript 類型定義
│   ├── package.json               # npm 配置
│   └── vite.config.ts             # Vite 配置
└── README.md                      # 專案說明
```

### 新增功能流程

1. **後端開發**

   - 在 `model` 包中定義實體類別
   - 在 `repository` 包中定義資料存取介面
   - 在 `service` 包中實作業務邏輯
   - 在 `controller` 包中定義 REST API 端點

2. **前端開發**

   - 在 `types` 中定義 TypeScript 介面
   - 在 `services` 中實作 API 呼叫
   - 在 `components` 中建立 UI 元件
   - 在 `pages` 中組合頁面

3. **測試**
   - 使用 Postman 或類似工具測試 API
   - 在瀏覽器中測試前端功能
   - 檢查控制台是否有錯誤訊息

## 故障排除

### 常見問題

1. **後端無法啟動**

   - 檢查 Java 版本是否為 17 或更高
   - 確認 Maven 已正確安裝
   - 檢查 8080 端口是否被佔用

2. **前端無法啟動**

   - 檢查 Node.js 版本是否為 18 或更高
   - 確認已執行 `npm install`
   - 檢查 3000 端口是否被佔用

3. **資料庫連接問題**

   - 確認 H2 資料庫配置正確
   - 檢查 `application.properties` 中的資料庫設定

4. **API 呼叫失敗**
   - 確認後端服務正在運行
   - 檢查 CORS 配置
   - 查看瀏覽器開發者工具中的網路請求

### 日誌查看

- **後端日誌**: 在終端機中查看 Spring Boot 啟動日誌
- **前端日誌**: 在瀏覽器開發者工具的控制台中查看

## 貢獻指南

1. Fork 此專案
2. 建立功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交變更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 開啟 Pull Request

## 授權

此專案採用 MIT 授權 - 詳見 [LICENSE](LICENSE) 檔案

## 聯絡資訊

如有問題或建議，請開啟 Issue 或聯絡專案維護者。

---

**注意**: 此專案僅供學習和開發練習使用，不應直接用於生產環境。
