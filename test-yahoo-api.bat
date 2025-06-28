@echo off
echo 測試 Yahoo Finance API...
echo.
echo 測試 AAPL (Apple):
curl -s "https://query1.finance.yahoo.com/v8/finance/chart/AAPL?interval=1d&range=1mo" > yahoo_test_aapl.json
echo AAPL 回應大小: 
dir yahoo_test_aapl.json
echo.
echo 測試 TSLA (Tesla):
curl -s "https://query1.finance.yahoo.com/v8/finance/chart/TSLA?interval=1d&range=1mo" > yahoo_test_tsla.json
echo TSLA 回應大小:
dir yahoo_test_tsla.json
echo.
echo 測試 GOOGL (Google):
curl -s "https://query1.finance.yahoo.com/v8/finance/chart/GOOGL?interval=1d&range=1mo" > yahoo_test_googl.json
echo GOOGL 回應大小:
dir yahoo_test_googl.json
echo.
echo 檢查網路連接:
ping -n 1 query1.finance.yahoo.com
echo.
echo 測試完成！ 