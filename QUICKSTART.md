# 快速開始指南

## 🚀 快速啟動

### 方法一：使用啟動腳本（推薦）

#### Windows 用戶

1. 雙擊 `start-backend.bat` 啟動後端
2. 開啟新的命令提示字元視窗
3. 雙擊 `start-frontend.bat` 啟動前端

#### Linux/Mac 用戶

1. 在終端機中執行：`chmod +x start-backend.sh start-frontend.sh`
2. 執行：`./start-backend.sh` 啟動後端
3. 開啟新的終端機視窗
4. 執行：`./start-frontend.sh` 啟動前端

### 方法二：手動啟動

#### 啟動後端

```bash
cd stock-project-backend
mvn clean install
mvn spring-boot:run
```

#### 啟動前端

```bash
cd stock-project-frontend
npm install
npm run dev
```

## 📱 存取應用程式

- **前端應用**: http://localhost:3000
- **後端 API**: http://localhost:8080
- **H2 資料庫控制台**: http://localhost:8080/h2-console
- **健康檢查**: http://localhost:8080/actuator/health

## 🔧 H2 資料庫登入資訊

- **JDBC URL**: `jdbc:h2:mem:stockdb`
- **使用者名稱**: `sa`
- **密碼**: `password`

## 📋 功能測試

1. **瀏覽股票列表**: 訪問首頁查看所有股票
2. **搜尋股票**: 在搜尋框輸入 "AAPL" 或 "Apple"
3. **新增到觀察清單**: 點擊股票旁的「新增到觀察清單」按鈕
4. **查看觀察清單**: 點擊導航欄的「觀察清單」
5. **移除股票**: 在觀察清單中點擊 X 按鈕移除股票

## 🐛 常見問題

### 後端無法啟動

- 確認 Java 17+ 已安裝：`java -version`
- 確認 Maven 已安裝：`mvn --version`
- 檢查 8080 端口是否被佔用

### 前端無法啟動

- 確認 Node.js 18+ 已安裝：`node --version`
- 確認 npm 已安裝：`npm --version`
- 檢查 3000 端口是否被佔用

### API 連接失敗

- 確認後端已啟動並運行在 8080 端口
- 檢查瀏覽器控制台是否有 CORS 錯誤
- 確認前端 API 配置中的 baseURL 正確

## 📚 下一步

- 閱讀完整的 [README.md](README.md) 了解專案架構
- 查看 [API 文件](README.md#api-文件) 了解所有端點
- 探索程式碼結構和擴展功能
