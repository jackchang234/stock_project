import React, { useState } from 'react';
import SearchBar from '../components/SearchBar';
import StockList from '../components/StockList';

/**
 * 首頁元件
 * 
 * 顯示股票搜尋和列表功能的主頁面。
 */
const Home: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');

  const handleSearch = (query: string) => {
    setSearchQuery(query);
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-6xl mx-auto">
        {/* 頁面標題 */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            股票市場初學者專案
          </h1>
          <p className="text-lg text-gray-600">
            探索股票市場，管理您的觀察清單
          </p>
        </div>

        {/* 搜尋區域 */}
        <div className="mb-8">
          <div className="flex flex-col items-center space-y-4">
            <h2 className="text-2xl font-semibold text-gray-900">
              搜尋股票
            </h2>
            <SearchBar onSearch={handleSearch} />
            <p className="text-sm text-gray-500 text-center">
              輸入股票代碼（如 AAPL）或公司名稱（如 Apple）進行搜尋
            </p>
          </div>
        </div>

        {/* 股票列表 */}
        <div className="mb-8">
          <h2 className="text-2xl font-semibold text-gray-900 mb-4">
            {searchQuery ? `搜尋結果: "${searchQuery}"` : '所有股票'}
          </h2>
          <StockList searchQuery={searchQuery} />
        </div>

        {/* 功能說明 */}
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-blue-900 mb-3">
            💡 使用說明
          </h3>
          <ul className="space-y-2 text-blue-800">
            <li>• 使用搜尋框搜尋特定的股票代碼或公司名稱</li>
            <li>• 點擊「新增到觀察清單」按鈕將股票加入您的觀察清單</li>
            <li>• 在導航欄中點擊「觀察清單」查看您關注的股票</li>
            <li>• 所有股票價格均為模擬資料，僅供學習使用</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Home; 