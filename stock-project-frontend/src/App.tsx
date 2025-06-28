import React from 'react';
import { Routes, Route, Link, useLocation } from 'react-router-dom';
import Home from './pages/Home';
import WatchlistPage from './pages/WatchlistPage';
import StockDetailPage from './pages/StockDetailPage';

/**
 * 主應用程式元件
 * 
 * 包含路由配置、導航欄和主要頁面結構。
 * 版本: 1.1 - 新增股票詳情頁面路由
 */
const App: React.FC = () => {
  const location = useLocation();

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 導航欄 */}
      <nav className="bg-white shadow-sm border-b border-gray-200">
        <div className="container mx-auto px-4">
          <div className="flex justify-between items-center h-16">
            {/* Logo */}
            <div className="flex items-center">
              <Link to="/" className="flex items-center space-x-2">
                <div className="w-8 h-8 bg-primary-600 rounded-lg flex items-center justify-center">
                  <svg className="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                  </svg>
                </div>
                <span className="text-xl font-bold text-gray-900">股票市場</span>
              </Link>
            </div>

            {/* 導航連結 */}
            <div className="flex space-x-8">
              <Link
                to="/"
                className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                  location.pathname === '/'
                    ? 'bg-primary-100 text-primary-700'
                    : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                }`}
              >
                首頁
              </Link>
              <Link
                to="/watchlist"
                className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                  location.pathname === '/watchlist'
                    ? 'bg-primary-100 text-primary-700'
                    : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                }`}
              >
                觀察清單
              </Link>
            </div>

            {/* 右側資訊 */}
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-500">
                歡迎，訪客用戶
              </span>
            </div>
          </div>
        </div>
      </nav>

      {/* 主要內容區域 */}
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/watchlist" element={<WatchlistPage />} />
          <Route path="/stock/:id" element={<StockDetailPage />} />
        </Routes>
      </main>

      {/* 頁腳 */}
      <footer className="bg-white border-t border-gray-200 mt-16">
        <div className="container mx-auto px-4 py-8">
          <div className="text-center">
            <p className="text-gray-600">
              © 2024 股票市場初學者專案. 這是一個學習專案，所有資料均為模擬資料。
            </p>
            <p className="text-sm text-gray-500 mt-2">
              使用 Spring Boot + React + TypeScript 建構 | 版本 1.1 - 新增歷史股價功能
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default App; 