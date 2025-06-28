-- 股票市場初學者專案 - 資料庫初始化腳本
-- 版本: 1.1 - 新增歷史股價功能
-- 作者: Stock Project Team

-- 插入範例股票資料
INSERT INTO stock (symbol, name, price) VALUES 
('AAPL', 'Apple Inc.', 150.25),
('GOOGL', 'Alphabet Inc.', 2750.50),
('MSFT', 'Microsoft Corporation', 310.75),
('AMZN', 'Amazon.com Inc.', 3300.00),
('TSLA', 'Tesla Inc.', 850.30),
('META', 'Meta Platforms Inc.', 320.45),
('NVDA', 'NVIDIA Corporation', 450.80),
('NFLX', 'Netflix Inc.', 580.20),
('JPM', 'JPMorgan Chase & Co.', 150.90),
('V', 'Visa Inc.', 220.15);

-- 注意: 歷史價格數據將通過 API 動態生成，不在此腳本中預載入
-- 使用 POST /api/stock-prices/{stockId}/generate-mock-data 來生成模擬歷史數據 