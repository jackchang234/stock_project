import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  ChartOptions,
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import { Stock, StockPrice, TimePeriod } from '../types';
import { stockApi, stockPriceApi, watchlistApi } from '../services/api';

// 註冊 Chart.js 元件
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

/**
 * 股票詳情頁面元件
 * 
 * 顯示股票的詳細資訊，包括歷史價格線圖、時間範圍選擇、
 * 觀察清單管理等功能。支援不同時間範圍的歷史數據顯示。
 * 
 * @version 1.1 - 新增歷史股價功能
 */
const StockDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  
  // 狀態管理
  const [stock, setStock] = useState<Stock | null>(null);
  const [stockPrices, setStockPrices] = useState<StockPrice[]>([]);
  const [allStockPrices, setAllStockPrices] = useState<StockPrice[]>([]); // 儲存所有原始數據
  const [selectedPeriod, setSelectedPeriod] = useState<string>('ALL');
  const [isInWatchlist, setIsInWatchlist] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [generatingData, setGeneratingData] = useState(false);

  // 支援的時間範圍（根據 Alpha Vantage API 實際回傳的每日數據）
  const timePeriods: { value: string; label: string; days: number }[] = [
    { value: '1M', label: '1個月', days: 30 },
    { value: '2M', label: '2個月', days: 60 },
    { value: '3M', label: '3個月', days: 90 },
    { value: 'ALL', label: '全部數據', days: 100 }, // Alpha Vantage 免費版最多約 100 筆
  ];

  // 載入股票基本資訊
  useEffect(() => {
    if (!id) return;
    
    const loadStock = async () => {
      try {
        setLoading(true);
        const stockData = await stockApi.getStockById(parseInt(id));
        setStock(stockData);
        console.log('股票資料載入成功:', stockData);
        
        // 檢查是否在觀察清單中
        const inWatchlist = await watchlistApi.checkInWatchlist(stockData.id);
        setIsInWatchlist(inWatchlist);
      } catch (err) {
        setError('載入股票資訊失敗');
        console.error('載入股票資訊錯誤:', err);
      } finally {
        setLoading(false);
      }
    };

    loadStock();
  }, [id]);

  // 生成模擬數據
  const handleGenerateMockData = async () => {
    if (!stock) return;
    
    try {
      setGeneratingData(true);
      await stockPriceApi.generateMockData(stock.id, 365);
      
      // 重新載入歷史價格數據
      const prices = await stockPriceApi.getStockPricesByPeriod(stock.id, selectedPeriod);
      setStockPrices(prices);
      setError(null);
    } catch (err) {
      setError('生成模擬數據失敗');
      console.error('生成模擬數據錯誤:', err);
    } finally {
      setGeneratingData(false);
    }
  };

  // 切換觀察清單狀態
  const handleToggleWatchlist = async () => {
    if (!stock) return;
    
    try {
      if (isInWatchlist) {
        await watchlistApi.removeFromWatchlist(stock.id);
        setIsInWatchlist(false);
      } else {
        await watchlistApi.addToWatchlist(stock.id);
        setIsInWatchlist(true);
      }
    } catch (err) {
      setError('操作觀察清單失敗');
      console.error('操作觀察清單錯誤:', err);
    }
  };

  // 處理時間範圍變更
  const handlePeriodChange = (period: string) => {
    setSelectedPeriod(period);
    
    // 根據選擇的時間範圍篩選數據
    const selectedPeriodData = timePeriods.find(p => p.value === period);
    if (selectedPeriodData && allStockPrices.length > 0) {
      if (selectedPeriodData.value !== 'ALL') {
        const filteredPrices = allStockPrices.slice(0, selectedPeriodData.days);
        setStockPrices(filteredPrices);
        console.log(`切換到 ${selectedPeriodData.label} 數據: ${filteredPrices.length} 筆`);
      } else {
        setStockPrices(allStockPrices);
        console.log(`切換到全部數據: ${allStockPrices.length} 筆`);
      }
    }
  };

  // 準備圖表數據 - 根據實際數據優化顯示
  const chartData = {
    labels: stockPrices.map(price => {
      const date = new Date(price.date);
      return date.toLocaleDateString('zh-TW', { 
        month: 'short', 
        day: 'numeric' 
      });
    }).reverse(), // 反轉讓最新日期在右邊
    datasets: [
      {
        label: '收盤價',
        data: stockPrices.map(price => price.closePrice).reverse(),
        borderColor: 'rgb(59, 130, 246)',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        borderWidth: 2,
        tension: 0.1,
        pointRadius: stockPrices.length > 50 ? 0 : 3, // 數據多時不顯示點
        pointHoverRadius: 6,
      },
      {
        label: '開盤價',
        data: stockPrices.map(price => price.openPrice).reverse(),
        borderColor: 'rgb(34, 197, 94)',
        backgroundColor: 'rgba(34, 197, 94, 0.1)',
        borderWidth: 1,
        tension: 0.1,
        pointRadius: 0,
        pointHoverRadius: 6,
      },
    ],
  };

  // 圖表選項 - 根據實際數據優化
  const chartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top' as const,
        labels: {
          usePointStyle: true,
          padding: 20,
        },
      },
      title: {
        display: true,
        text: `${stock?.name} (${stock?.symbol}) - ${timePeriods.find(p => p.value === selectedPeriod)?.label || '歷史價格'} (${stockPrices.length} 筆數據)`,
        font: {
          size: 16,
          weight: 'bold',
        },
        padding: 20,
      },
      tooltip: {
        mode: 'index' as const,
        intersect: false,
        callbacks: {
          title: (context) => {
            const date = new Date(stockPrices[stockPrices.length - 1 - context[0].dataIndex].date);
            return date.toLocaleDateString('zh-TW', { 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            });
          },
          label: (context) => {
            return `${context.dataset.label}: $${context.parsed.y.toFixed(2)}`;
          },
        },
      },
    },
    scales: {
      y: {
        beginAtZero: false,
        title: {
          display: true,
          text: '價格 (USD)',
          font: {
            weight: 'bold',
          },
        },
        grid: {
          color: 'rgba(0, 0, 0, 0.1)',
        },
        ticks: {
          callback: (value) => `$${Number(value).toFixed(0)}`,
        },
      },
      x: {
        title: {
          display: true,
          text: '日期',
          font: {
            weight: 'bold',
          },
        },
        grid: {
          color: 'rgba(0, 0, 0, 0.1)',
        },
        ticks: {
          maxTicksLimit: stockPrices.length > 30 ? 10 : 20, // 根據數據量調整刻度
        },
      },
    },
    interaction: {
      mode: 'nearest' as const,
      axis: 'x' as const,
      intersect: false,
    },
  };

  const fetchStockPrices = async (symbol: string, period: string) => {
    setLoading(true);
    setError('');
    
    try {
      // 首先嘗試取得模擬數據
      console.log('嘗試取得模擬歷史價格數據...');
      const mockPrices = await stockPriceApi.getStockPrices(stock?.id || 0);
      
      if (Array.isArray(mockPrices) && mockPrices.length > 0) {
        console.log('找到模擬數據:', mockPrices.length, '筆');
        setAllStockPrices(mockPrices);
        
        // 根據選擇的時間範圍篩選數據
        const selectedPeriodData = timePeriods.find(p => p.value === selectedPeriod);
        if (selectedPeriodData && selectedPeriodData.value !== 'ALL') {
          const filteredPrices = mockPrices.slice(0, selectedPeriodData.days);
          setStockPrices(filteredPrices);
          console.log(`顯示 ${selectedPeriodData.label} 數據: ${filteredPrices.length} 筆`);
        } else {
          setStockPrices(mockPrices);
          console.log(`顯示全部數據: ${mockPrices.length} 筆`);
        }
        
        console.log('成功使用模擬數據');
        return;
      }
      
      // 如果沒有模擬數據，嘗試使用 Alpha Vantage API
      console.log('沒有模擬數據，嘗試使用 Alpha Vantage API...');
      const apiKey = 'FB5DB5KB2W1FPDD9';
      const prices = await stockPriceApi.getRealStockPricesFromAlphaVantage(symbol, apiKey);
      
      if (Array.isArray(prices) && prices.length > 0) {
        // 檢查資料格式
        const sample = prices[0];
        if (
          typeof sample.date === 'string' &&
          typeof sample.openPrice === 'number' &&
          typeof sample.closePrice === 'number' &&
          typeof sample.highPrice === 'number' &&
          typeof sample.lowPrice === 'number' &&
          typeof sample.volume === 'number'
        ) {
          setAllStockPrices(prices);
          
          // 根據選擇的時間範圍篩選數據
          const selectedPeriodData = timePeriods.find(p => p.value === selectedPeriod);
          if (selectedPeriodData && selectedPeriodData.value !== 'ALL') {
            const filteredPrices = prices.slice(0, selectedPeriodData.days);
            setStockPrices(filteredPrices);
            console.log(`顯示 ${selectedPeriodData.label} 數據: ${filteredPrices.length} 筆`);
          } else {
            setStockPrices(prices);
            console.log(`顯示全部數據: ${prices.length} 筆`);
          }
          
          console.log(`成功取得 ${prices.length} 筆真實歷史價格數據`);
        } else {
          throw new Error('API 回傳資料格式不符');
        }
      } else {
        throw new Error('API 回傳為空');
      }
    } catch (error) {
      console.error('取得歷史價格失敗:', error);
      
      // 提供更友善的錯誤訊息和解決方案
      if (error instanceof Error) {
        if (error.message.includes('API 回傳為空')) {
          setError('Alpha Vantage API 已達使用限制。建議使用模擬數據進行測試。');
        } else if (error.message.includes('API 回傳資料格式不符')) {
          setError('API 資料格式錯誤，建議使用模擬數據。');
        } else {
          setError('網路連線問題，建議使用模擬數據。');
        }
      } else {
        setError('取得歷史價格數據失敗，建議使用模擬數據。');
      }
      
      setStockPrices([]);
      setAllStockPrices([]);
    } finally {
      setLoading(false);
    }
  };

  // 確保 fetchStockPrices 有被 useEffect 呼叫
  useEffect(() => {
    console.log('useEffect 觸發，stock:', stock, 'selectedPeriod:', selectedPeriod);
    if (stock && stock.symbol) {
      console.log('開始呼叫 fetchStockPrices，股票代碼:', stock.symbol);
      fetchStockPrices(stock.symbol, selectedPeriod);
    } else {
      console.log('stock 或 stock.symbol 為空，無法呼叫 fetchStockPrices');
    }
  }, [stock?.symbol, selectedPeriod]);

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">載入中...</p>
        </div>
      </div>
    );
  }

  if (!stock) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">股票不存在</h2>
          <button
            onClick={() => navigate('/')}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            返回首頁
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 導航欄 */}
      <div className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <button
              onClick={() => navigate('/')}
              className="text-gray-600 hover:text-gray-900 flex items-center"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
              返回首頁
            </button>
            <h1 className="text-xl font-semibold text-gray-900">股票詳情</h1>
            <div className="w-20"></div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* 股票基本資訊 */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <div className="flex justify-between items-start">
            <div>
              <h2 className="text-2xl font-bold text-gray-900">{stock.name}</h2>
              <p className="text-lg text-gray-600">{stock.symbol}</p>
              {allStockPrices.length > 0 ? (() => {
                // 找到日期最新的那一筆
                const sorted = [...allStockPrices].sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
                const latest = sorted[sorted.length - 1];
                const prev = sorted.length > 1 ? sorted[sorted.length - 2] : null;
                const diff = prev ? latest.closePrice - prev.closePrice : 0;
                const percent = prev ? (diff / prev.closePrice) * 100 : 0;
                return (
                  <div className="flex items-end space-x-2 mt-2">
                    <span className="text-3xl font-bold text-green-600">
                      ${latest.closePrice.toFixed(2)}
                    </span>
                    <span className="text-xs text-gray-500 mb-1">
                      （{new Date(latest.date).toLocaleDateString('zh-TW')} 收盤）
                    </span>
                    {prev && (
                      <span className={`ml-2 text-sm font-bold ${diff > 0 ? 'text-red-600' : diff < 0 ? 'text-blue-600' : 'text-gray-600'}`}>
                        {diff > 0 ? '+' : ''}{diff.toFixed(2)} ({diff > 0 ? '+' : ''}{percent.toFixed(2)}%)
                      </span>
                    )}
                  </div>
                );
              })() : (
                <p className="text-3xl font-bold text-green-600 mt-2">${stock.price.toFixed(2)}</p>
              )}
            </div>
            <div className="flex space-x-3">
              <button
                onClick={handleToggleWatchlist}
                className={`px-4 py-2 rounded-lg font-medium ${
                  isInWatchlist
                    ? 'bg-red-100 text-red-700 hover:bg-red-200'
                    : 'bg-blue-100 text-blue-700 hover:bg-blue-200'
                }`}
              >
                {isInWatchlist ? '移除觀察清單' : '加入觀察清單'}
              </button>
              <button
                onClick={() => navigate('/watchlist')}
                className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 font-medium"
              >
                查看觀察清單
              </button>
            </div>
          </div>
        </div>

        {/* 錯誤訊息 - 優化顯示 */}
        {error && (
          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-6 mb-6">
            <div className="flex">
              <div className="flex-shrink-0">
                <svg className="h-6 w-6 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                </svg>
              </div>
              <div className="ml-4 flex-1">
                <h3 className="text-lg font-medium text-yellow-800 mb-2">無法取得真實歷史價格數據</h3>
                <p className="text-sm text-yellow-700 mb-4">{error}</p>
                
                {/* 提供解決方案 */}
                <div className="bg-yellow-100 rounded-lg p-4">
                  <h4 className="font-medium text-yellow-800 mb-3">解決方案：</h4>
                  <div className="space-y-3">
                    <div>
                      <p className="text-sm text-yellow-700 mb-2">
                        <strong>選項 1：</strong> 生成模擬數據進行測試
                      </p>
                      <button
                        onClick={handleGenerateMockData}
                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 text-sm font-medium"
                      >
                        生成模擬歷史價格數據
                      </button>
                    </div>
                    
                    <div>
                      <p className="text-sm text-yellow-700 mb-2">
                        <strong>選項 2：</strong> 等待 API 限制重置
                      </p>
                      <ul className="text-xs text-yellow-700 space-y-1 ml-4">
                        <li>• Alpha Vantage 免費版：每天 25 次請求限制</li>
                        <li>• 重置時間：每天 UTC 00:00（台灣時間 08:00）</li>
                        <li>• 建議：註冊多個帳戶或升級付費版</li>
                      </ul>
                    </div>
                    
                    <div>
                      <p className="text-sm text-yellow-700 mb-2">
                        <strong>選項 3：</strong> 嘗試其他股票代碼
                      </p>
                      <p className="text-xs text-yellow-700 ml-4">
                        建議使用：AAPL、MSFT、GOOGL、TSLA、META
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* 時間範圍選擇 */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">選擇時間範圍</h3>
          <div className="flex space-x-2">
            {timePeriods.map((period) => (
              <button
                key={period.value}
                onClick={() => handlePeriodChange(period.value)}
                className={`px-4 py-2 rounded-lg font-medium ${
                  selectedPeriod === period.value
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
              >
                {period.label}
              </button>
            ))}
          </div>
        </div>

        {/* 股票線圖 */}
        {stockPrices.length > 0 && (
          <div className="bg-white rounded-lg shadow-sm p-6">
            <div className="h-96">
              <Line data={chartData} options={chartOptions} />
            </div>
          </div>
        )}

        {/* 數據統計 - 根據實際數據優化顯示 */}
        {stockPrices.length > 0 && (
          <div className="bg-white rounded-lg shadow-sm p-6 mt-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">數據統計</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="text-center p-4 bg-blue-50 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">最高價</p>
                <p className="text-xl font-bold text-green-600">
                  ${Math.max(...stockPrices.map(p => p.highPrice)).toFixed(2)}
                </p>
                <p className="text-xs text-gray-500 mt-1">
                  {new Date(stockPrices.find(p => p.highPrice === Math.max(...stockPrices.map(p => p.highPrice)))?.date || '').toLocaleDateString('zh-TW')}
                </p>
              </div>
              <div className="text-center p-4 bg-red-50 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">最低價</p>
                <p className="text-xl font-bold text-red-600">
                  ${Math.min(...stockPrices.map(p => p.lowPrice)).toFixed(2)}
                </p>
                <p className="text-xs text-gray-500 mt-1">
                  {new Date(stockPrices.find(p => p.lowPrice === Math.min(...stockPrices.map(p => p.lowPrice)))?.date || '').toLocaleDateString('zh-TW')}
                </p>
              </div>
              <div className="text-center p-4 bg-green-50 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">最新收盤價</p>
                <p className="text-xl font-bold text-blue-600">
                  ${stockPrices[stockPrices.length - 1]?.closePrice.toFixed(2) || 'N/A'}
                </p>
                <p className="text-xs text-gray-500 mt-1">
                  {stockPrices[stockPrices.length - 1] ? new Date(stockPrices[stockPrices.length - 1].date).toLocaleDateString('zh-TW') : 'N/A'}
                </p>
              </div>
              <div className="text-center p-4 bg-purple-50 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">平均成交量</p>
                <p className="text-xl font-bold text-purple-600">
                  {Math.round(stockPrices.reduce((sum, p) => sum + p.volume, 0) / stockPrices.length).toLocaleString()}
                </p>
                <p className="text-xs text-gray-500 mt-1">股</p>
              </div>
            </div>
            
            {/* 價格變化統計 */}
            {stockPrices.length > 1 && (
              <div className="mt-6 pt-6 border-t border-gray-200">
                <h4 className="text-md font-semibold text-gray-900 mb-3">價格變化</h4>
                <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
                  <div className="text-center">
                    <p className="text-sm text-gray-600">期間漲跌</p>
                    <p className={`text-lg font-bold ${stockPrices[0].closePrice >= stockPrices[stockPrices.length - 1].closePrice ? 'text-green-600' : 'text-red-600'}`}>
                      {stockPrices[0].closePrice >= stockPrices[stockPrices.length - 1].closePrice ? '+' : ''}
                      ${(stockPrices[0].closePrice - stockPrices[stockPrices.length - 1].closePrice).toFixed(2)}
                    </p>
                  </div>
                  <div className="text-center">
                    <p className="text-sm text-gray-600">漲跌幅</p>
                    <p className={`text-lg font-bold ${stockPrices[0].closePrice >= stockPrices[stockPrices.length - 1].closePrice ? 'text-green-600' : 'text-red-600'}`}>
                      {stockPrices[0].closePrice >= stockPrices[stockPrices.length - 1].closePrice ? '+' : ''}
                      {(((stockPrices[0].closePrice - stockPrices[stockPrices.length - 1].closePrice) / stockPrices[stockPrices.length - 1].closePrice) * 100).toFixed(2)}%
                    </p>
                  </div>
                  <div className="text-center">
                    <p className="text-sm text-gray-600">數據期間</p>
                    <p className="text-lg font-bold text-gray-800">
                      {new Date(stockPrices[stockPrices.length - 1].date).toLocaleDateString('zh-TW')} - {new Date(stockPrices[0].date).toLocaleDateString('zh-TW')}
                    </p>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default StockDetailPage; 