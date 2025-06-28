import React from 'react';
import Watchlist from '../components/Watchlist';

/**
 * 觀察清單頁面元件
 * 
 * 顯示用戶的觀察清單頁面。
 */
const WatchlistPage: React.FC = () => {
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-6xl mx-auto">
        {/* 頁面標題 */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">
            我的觀察清單
          </h1>
          <p className="text-lg text-gray-600">
            管理您關注的股票
          </p>
        </div>

        {/* 觀察清單內容 */}
        <Watchlist />

        {/* 功能說明 */}
        <div className="mt-8 bg-green-50 border border-green-200 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-green-900 mb-3">
            📊 觀察清單功能
          </h3>
          <ul className="space-y-2 text-green-800">
            <li>• 查看您已新增到觀察清單的所有股票</li>
            <li>• 點擊右上角的 X 按鈕可以從觀察清單中移除股票</li>
            <li>• 觀察清單會即時更新，反映最新的股票資訊</li>
            <li>• 返回首頁可以繼續搜尋和新增更多股票</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default WatchlistPage; 