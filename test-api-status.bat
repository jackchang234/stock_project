@echo off
echo ========================================
echo Alpha Vantage API 狀態檢查
echo ========================================
echo.

echo 1. 檢查 IP 地址...
curl -s "https://api.ipify.org?format=json"
echo.
echo.

echo 2. 測試舊 API Key...
curl -s "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&apikey=T46XCMLA36IGBS6R" | findstr "Information"
echo.

echo 3. 測試新 API Key...
curl -s "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&apikey=FB5DB5KB2W1FPDD9" | findstr "Information"
echo.

echo 4. 測試不同 API 函數...
curl -s "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=test&apikey=FB5DB5KB2W1FPDD9" | findstr "Information"
echo.

echo 5. 檢查後端 API...
curl -s "http://localhost:8080/api/stocks" | findstr "AAPL"
echo.

echo.
echo ========================================
echo 檢查完成
echo ========================================
pause 