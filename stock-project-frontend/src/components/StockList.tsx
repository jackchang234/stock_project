import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { Stock } from '../types';
import { stockApi, watchlistApi } from '../services/api';

interface StockListProps {
  searchQuery?: string;
}

/**
 * 股票列表元件
 * 
 * 顯示股票列表，支援搜尋和新增到觀察清單功能。
 * 
 * @param searchQuery 搜尋關鍵字
 */
const StockList: React.FC<StockListProps> = ({ searchQuery = '' }) => {
  const [stocks, setStocks] = useState<Stock[]>([]);
  const [loading, setLoading] = useState(true);
  const [watchlistStatus, setWatchlistStatus] = useState<Record<number, boolean>>({});

  // 載入股票資料
  const loadStocks = async (query: string) => {
    try {
      setLoading(true);
      let stockData: Stock[];
      
      if (query.trim()) {
        stockData = await stockApi.searchStocks(query);
      } else {
        stockData = await stockApi.getAllStocks();
      }
      
      setStocks(stockData);
      
      // 檢查每支股票是否在觀察清單中
      const statusMap: Record<number, boolean> = {};
      for (const stock of stockData) {
        try {
          statusMap[stock.id] = await watchlistApi.checkInWatchlist(stock.id);
        } catch (error) {
          console.error(`檢查股票 ${stock.id} 觀察清單狀態失敗:`, error);
          statusMap[stock.id] = false;
        }
      }
      setWatchlistStatus(statusMap);
    } catch (error) {
      console.error('載入股票資料失敗:', error);
      toast.error('載入股票資料失敗，請稍後再試');
    } finally {
      setLoading(false);
    }
  };

  // 新增股票到觀察清單
  const handleAddToWatchlist = async (stock: Stock) => {
    try {
      await watchlistApi.addToWatchlist(stock.id);
      setWatchlistStatus(prev => ({ ...prev, [stock.id]: true }));
      toast.success(`${stock.symbol} 已新增到觀察清單`);
    } catch (error) {
      console.error('新增到觀察清單失敗:', error);
      toast.error('新增到觀察清單失敗，請稍後再試');
    }
  };

  // 當搜尋關鍵字改變時重新載入資料
  useEffect(() => {
    loadStocks(searchQuery);
  }, [searchQuery]);

  if (loading) {
    return (
      <div className="flex justify-center items-center py-8">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
        <span className="ml-2 text-gray-600">載入中...</span>
      </div>
    );
  }

  if (stocks.length === 0) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-500">沒有找到符合條件的股票</p>
      </div>
    );
  }

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white border border-gray-200 rounded-lg">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              股票代碼
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              公司名稱
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              價格
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {stocks.map((stock) => (
            <tr key={stock.id} className="hover:bg-gray-50">
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm font-medium text-gray-900">{stock.symbol}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{stock.name}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm font-semibold text-gray-900">
                  ${stock.price.toFixed(2)}
                </div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                {watchlistStatus[stock.id] ? (
                  <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-success-100 text-success-800">
                    已在觀察清單
                  </span>
                ) : (
                  <button
                    onClick={() => handleAddToWatchlist(stock)}
                    className="btn-success text-xs"
                  >
                    新增到觀察清單
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default StockList; 