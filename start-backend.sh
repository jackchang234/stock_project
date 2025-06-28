#!/bin/bash
echo "啟動股票市場專案後端..."
echo
cd stock-project-backend
echo "正在建置專案..."
mvn clean install
echo
echo "正在啟動 Spring Boot 應用程式..."
mvn spring-boot:run 