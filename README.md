# 股票市場初學者專案

## 專案概述

這是一個專為股票市場初學者設計的 Web 應用程式，允許用戶查看股票資訊、管理觀察清單，並追蹤基本股票數據（如價格、名稱、代碼）。

### 技術架構

- **後端**: Spring Boot 3.x (Java) 搭配 Spring Web、Spring Data JPA 和 H2 記憶體資料庫
- **前端**: React 18.x 搭配 TypeScript，使用 Axios 進行 API 呼叫，Tailwind CSS 進行樣式設計
- **API**: RESTful API 連接後端和前端

### 初始功能

- 顯示股票列表（代碼、名稱、當前價格）
- 允許用戶新增/移除觀察清單中的股票
- 基本搜尋功能，可依代碼或名稱搜尋股票
- 簡潔、響應式的用戶介面

### 未來擴展性

程式碼庫採用模組化設計且文件完整，可輕鬆新增功能如用戶認證、即時股票數據或投資組合追蹤。

## 功能特色

- ✅ 股票列表顯示
- ✅ 觀察清單管理
- ✅ 股票搜尋功能
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
│   ├── controller/          # REST API 控制器
│   ├── service/            # 業務邏輯層
│   ├── repository/         # 資料存取層
│   ├── model/              # 實體和 DTO
│   │   ├── entity/         # JPA 實體
│   │   └── dto/            # 資料傳輸物件
│   ├── config/             # 配置類別
│   └── StockProjectApplication.java
├── src/main/resources/
│   ├── application.properties  # 應用程式配置
│   └── data.sql               # 初始資料
└── pom.xml                    # Maven 配置
```

### 前端結構

```
stock-project-frontend/
├── src/
│   ├── components/          # React 元件
│   ├── pages/              # 頁面元件
│   ├── services/           # API 服務
│   ├── App.tsx             # 主應用元件
│   └── main.tsx            # 應用程式入口
├── package.json            # npm 配置
├── vite.config.ts          # Vite 配置
├── tailwind.config.js      # Tailwind CSS 配置
└── tsconfig.json           # TypeScript 配置
```

## 除錯技巧

### 後端除錯

1. **檢查日誌**

   - 查看控制台輸出的 Spring Boot 日誌
   - 啟用除錯日誌: 在 `application.properties` 中設定 `logging.level.org.springframework=DEBUG`

2. **資料庫檢查**

   - 使用 H2 控制台: http://localhost:8080/h2-console
   - 檢查 `data.sql` 檔案確認初始資料

3. **API 測試**
   - 使用 Postman 或 curl 測試 API 端點
   - 檢查 HTTP 狀態碼和回應內容

### 前端除錯

1. **開發者工具**

   - 使用 React Developer Tools 檢查元件狀態
   - 查看瀏覽器控制台的錯誤訊息

2. **網路請求**

   - 在瀏覽器 Network 標籤中檢查 API 呼叫
   - 確認請求 URL 和回應狀態

3. **狀態管理**
   - 使用 React DevTools 檢查元件狀態變化
   - 在關鍵位置加入 console.log 進行除錯

## 未來增強功能

### 建議的新功能

1. **用戶認證**

   - Spring Security 搭配 JWT
   - 用戶註冊和登入功能
   - 個人化觀察清單

2. **即時股票數據**

   - 整合外部 API（如 Alpha Vantage、Yahoo Finance）
   - WebSocket 即時更新
   - 股票價格圖表

3. **投資組合追蹤**

   - 買賣交易記錄
   - 投資報酬率計算
   - 投資組合分析

4. **進階功能**
   - 股票篩選器
   - 技術指標
   - 新聞整合
   - 移動應用程式

### 擴展指南

#### 新增後端功能

1. 建立新的控制器、服務和儲存庫
2. 更新資料庫結構和 DTO
3. 新增單元測試
4. 更新 API 文件

#### 新增前端功能

1. 建立新的元件或頁面
2. 更新 API 服務 (`api.ts`)
3. 在 `App.tsx` 中新增路由
4. 更新樣式和響應式設計

## 貢獻指南

1. **建立分支**

   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **遵循架構**

   - 後端: 遵循分層架構（控制器、服務、儲存庫）
   - 前端: 保持元件模組化和可重用性

3. **測試**

   - 為新功能撰寫單元測試
   - 確保所有測試通過

4. **文件更新**
   - 更新 README 中的新端點或元件
   - 新增必要的註解和文件

## 授權

本專案採用 MIT 授權條款。詳見 [LICENSE](LICENSE) 檔案。

## 聯絡資訊

如有問題或建議，請開啟 GitHub Issue 或聯絡專案維護者。
