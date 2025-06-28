#!/bin/bash
# v1.1 新增：API 健康檢查腳本
# 執行方式：bash health-check.sh

API_BASE="http://localhost:8080/api"

function check_api() {
  local url=$1
  local desc=$2
  echo -n "檢查 $desc ... "
  resp=$(curl -s -w "HTTPSTATUS:%{http_code}" "$url")
  body=$(echo "$resp" | sed -e 's/HTTPSTATUS:.*//g')
  status=$(echo "$resp" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
  if [ "$status" = "200" ]; then
    echo "✅ 正常"
  else
    echo "❌ 錯誤 (HTTP $status)"
    echo "  URL: $url"
    echo "  回應: $body"
  fi
}

check_api "$API_BASE/stocks" "股票列表"
check_api "$API_BASE/stock-prices/1/period/1Y" "股票 1 歷史價格 (1Y)"
check_api "$API_BASE/stock-prices/1/has-data" "股票 1 是否有歷史數據"
check_api "$API_BASE/stock-prices/supported-periods" "支援的時間範圍"
check_api "$API_BASE/watchlist" "觀察清單" 