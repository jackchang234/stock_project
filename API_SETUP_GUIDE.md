# 📈 股票 API 註冊指南

## 🏆 推薦：Alpha Vantage (最簡單)

### 註冊步驟：

1. **前往註冊頁面**：https://www.alphavantage.co/support/#api-key
2. **填寫資料**：
   - Email：您的 email
   - 姓名：隨便填
3. **立即獲得 API Key**：註冊後馬上就能拿到
4. **無需信用卡**：完全免費

### 免費額度：

- 每月 500 次 API 呼叫
- 足夠個人使用和測試

### 使用方式：

```bash
# 測試 API (替換 YOUR_API_KEY)
curl "http://localhost:8080/api/stock-prices/alphavantage/AAPL?apiKey=YOUR_API_KEY"
```

## 🥈 備選：Finnhub

### 註冊步驟：

1. **前往**：https://finnhub.io/
2. **點擊**："Get free API key"
3. **填寫基本資料**
4. **獲得 API Key**

### 免費額度：

- 每月 60 次 API 呼叫

## 🔧 如何整合到專案

### 1. 取得 API Key 後，測試連接：

```bash
# 在專案目錄執行
curl "http://localhost:8080/api/stock-prices/alphavantage/AAPL?apiKey=YOUR_API_KEY"
```

### 2. 前端會自動使用真實數據：

- 股票詳情頁面會顯示真實歷史價格
- 線圖會顯示實際股價走勢

### 3. 支援的股票代碼：

- **美股**：AAPL, GOOGL, TSLA, MSFT, AMZN
- **台股**：2330.TW, 2317.TW, 2454.TW
- **全球**：其他國家股票代碼

## ⚠️ 注意事項

1. **API 限制**：免費版有呼叫次數限制
2. **網路環境**：確保能連接到外部 API
3. **股票代碼**：使用正確的股票代碼格式

## 🚀 快速開始

1. 註冊 Alpha Vantage 取得 API Key
2. 重啟後端服務
3. 測試 API 連接
4. 享受真實股票數據！

---

_如有問題，請檢查網路連接和 API Key 是否正確_
