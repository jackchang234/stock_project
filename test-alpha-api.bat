@echo off
echo 測試 Alpha Vantage API...

echo 1. 直接測試 Alpha Vantage API...
curl -s "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&apikey=FB5DB5KB2W1FPDD9" > direct_alpha_test.json
echo 直接 API 測試完成，結果儲存到 direct_alpha_test.json

echo.
echo 2. 測試後端 Alpha Vantage API...
curl -s "http://localhost:8080/api/stock-prices/alphavantage/AAPL?apiKey=FB5DB5KB2W1FPDD9" > backend_alpha_test.json
echo 後端 API 測試完成，結果儲存到 backend_alpha_test.json

echo.
echo 檢查檔案大小...
echo 直接 API 檔案大小:
dir direct_alpha_test.json | findstr "direct_alpha_test.json"
echo 後端 API 檔案大小:
dir backend_alpha_test.json | findstr "backend_alpha_test.json"

echo.
echo 測試完成！
pause 