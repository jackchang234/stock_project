@echo off
echo ========================================
echo 股票市場專案 - 自動啟動腳本
echo ========================================

echo 1. 啟動後端服務...
start "Spring Boot Backend" cmd /k "cd stock-project-backend && mvn spring-boot:run"

echo 2. 等待後端啟動...
timeout /t 30 /nobreak

echo 3. 啟動前端服務...
start "React Frontend" cmd /k "cd stock-project-frontend && npm run dev"

echo 4. 等待前端啟動...
timeout /t 15 /nobreak

echo 5. 檢查服務狀態...
echo 後端健康檢查: http://localhost:8080/actuator/health
echo 前端應用: http://localhost:3000
echo H2 資料庫: http://localhost:8080/h2-console

echo ========================================
echo 啟動完成！請開啟瀏覽器訪問：
echo 前端: http://localhost:3000
echo ========================================

pause 