import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { WatchlistItem } from '../types';
import { watchlistApi } from '../services/api';

/**
 * 觀察清單元件
 * 
 * 顯示用戶的觀察清單，支援移除股票和跳轉到詳情頁面功能。
 * 版本: 1.1 - 新增點擊跳轉到詳情頁面功能
 */
const Watchlist: React.FC = () => {
  const navigate = useNavigate();
  const [watchlist, setWatchlist] = useState<WatchlistItem[]>([]);
  const [loading, setLoading] = useState(true);

  // 載入觀察清單
  const loadWatchlist = async () => {
    try {
      setLoading(true);
      const data = await watchlistApi.getWatchlist();
      setWatchlist(data);
    } catch (error) {
      console.error('載入觀察清單失敗:', error);
      toast.error('載入觀察清單失敗，請稍後再試');
    } finally {
      setLoading(false);
    }
  };

  // 從觀察清單移除股票
  const handleRemoveFromWatchlist = async (stockId: number, stockSymbol: string, event: React.MouseEvent) => {
    event.stopPropagation(); // 防止觸發卡片點擊事件
    try {
      await watchlistApi.removeFromWatchlist(stockId);
      setWatchlist(prev => prev.filter(item => item.stockId !== stockId));
      toast.success(`${stockSymbol} 已從觀察清單移除`);
    } catch (error) {
      console.error('從觀察清單移除失敗:', error);
      toast.error('從觀察清單移除失敗，請稍後再試');
    }
  };

  // 跳轉到股票詳情頁面
  const handleCardClick = (stockId: number) => {
    navigate(`/stock/${stockId}`);
  };

  // 組件載入時取得觀察清單
  useEffect(() => {
    loadWatchlist();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center py-8">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
        <span className="ml-2 text-gray-600">載入中...</span>
      </div>
    );
  }

  if (watchlist.length === 0) {
    return (
      <div className="text-center py-8">
        <div className="text-gray-400 mb-4">
          <svg className="mx-auto h-12 w-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
        </div>
        <h3 className="text-lg font-medium text-gray-900 mb-2">觀察清單是空的</h3>
        <p className="text-gray-500">您還沒有新增任何股票到觀察清單</p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold text-gray-900">我的觀察清單</h2>
        <span className="text-sm text-gray-500">
          共 {watchlist.length} 支股票
        </span>
      </div>
      
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {watchlist.map((item) => (
          <div 
            key={item.id} 
            className="card p-4 cursor-pointer hover:shadow-md transition-shadow"
            onClick={() => handleCardClick(item.stock.id)}
          >
            <div className="flex justify-between items-start mb-3">
              <div>
                <h3 className="text-lg font-semibold text-gray-900">
                  {item.stock.symbol}
                </h3>
                <p className="text-sm text-gray-600">
                  {item.stock.name}
                </p>
              </div>
              <button
                onClick={(e) => handleRemoveFromWatchlist(item.stockId, item.stock.symbol, e)}
                className="text-danger-600 hover:text-danger-800 transition-colors"
                title="從觀察清單移除"
              >
                <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            
            <div className="flex justify-between items-center">
              <span className="text-2xl font-bold text-gray-900">
                ${item.stock.price.toFixed(2)}
              </span>
              <span className="text-xs text-gray-500">
                點擊查看詳情
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Watchlist; 