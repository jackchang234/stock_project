@echo off
echo ========================================
echo 股票市場專案 - 狀態檢查
echo ========================================

echo 檢查後端服務...
curl -s http://localhost:8080/actuator/health
if %errorlevel% equ 0 (
    echo ✅ 後端服務正常
) else (
    echo ❌ 後端服務異常
)

echo.
echo 檢查前端服務...
curl -s http://localhost:3000
if %errorlevel% equ 0 (
    echo ✅ 前端服務正常
) else (
    echo ❌ 前端服務異常
)

echo.
echo 檢查 API 端點...
echo 股票列表 API:
curl -s http://localhost:8080/api/stocks
if %errorlevel% equ 0 (
    echo ✅ 股票列表 API 正常
) else (
    echo ❌ 股票列表 API 異常
)

echo.
echo 檢查支援的時間範圍 API:
curl -s http://localhost:8080/api/stock-prices/supported-periods
if %errorlevel% equ 0 (
    echo ✅ 時間範圍 API 正常
) else (
    echo ❌ 時間範圍 API 異常
)

echo.
echo ========================================
echo 檢查完成！
echo ========================================
pause 