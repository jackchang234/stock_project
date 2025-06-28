@echo off
echo 啟動股票市場專案前端...
echo.
cd stock-project-frontend
echo 正在安裝依賴...
call npm install
echo.
echo 正在啟動開發伺服器...
call npm run dev
pause 