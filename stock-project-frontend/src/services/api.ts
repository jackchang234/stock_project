import axios from 'axios';
import { Stock, StockPrice, WatchlistItem, AddToWatchlistRequest, CheckWatchlistResponse, TimePeriod } from '../types';

// 建立 Axios 實例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 請求攔截器
api.interceptors.request.use(
  (config) => {
    console.log(`發送請求: ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('請求錯誤:', error);
    return Promise.reject(error);
  }
);

// 回應攔截器
api.interceptors.response.use(
  (response) => {
    console.log(`收到回應: ${response.status} ${response.config.url}`);
    return response;
  },
  (error) => {
    console.error('回應錯誤:', error);
    return Promise.reject(error);
  }
);

// 股票相關 API
export const stockApi = {
  // 取得所有股票
  getAllStocks: async (): Promise<Stock[]> => {
    const response = await api.get<Stock[]>('/stocks');
    return response.data;
  },

  // 搜尋股票
  searchStocks: async (query: string): Promise<Stock[]> => {
    const response = await api.get<Stock[]>(`/stocks/search?query=${encodeURIComponent(query)}`);
    return response.data;
  },

  // 根據 ID 取得股票
  getStockById: async (id: number): Promise<Stock> => {
    const response = await api.get<Stock>(`/stocks/${id}`);
    return response.data;
  },

  // 根據代碼取得股票
  getStockBySymbol: async (symbol: string): Promise<Stock> => {
    const response = await api.get<Stock>(`/stocks/symbol/${symbol}`);
    return response.data;
  },
};

// 股票歷史價格相關 API
export const stockPriceApi = {
  // 取得股票歷史價格數據
  getStockPrices: async (stockId: number): Promise<StockPrice[]> => {
    const response = await api.get<StockPrice[]>(`/stock-prices/${stockId}`);
    return response.data;
  },

  // 根據時間範圍取得股票歷史價格數據
  getStockPricesByPeriod: async (stockId: number, period: TimePeriod): Promise<StockPrice[]> => {
    const response = await api.get<StockPrice[]>(`/stock-prices/${stockId}/period/${period}`);
    return response.data;
  },

  // 生成模擬歷史價格數據
  generateMockData: async (stockId: number, days: number = 365): Promise<string> => {
    const response = await api.post<string>(`/stock-prices/${stockId}/generate-mock-data?days=${days}`);
    return response.data;
  },

  // 檢查股票是否有歷史價格數據
  hasHistoricalData: async (stockId: number): Promise<boolean> => {
    const response = await api.get<boolean>(`/stock-prices/${stockId}/has-data`);
    return response.data;
  },

  // 刪除股票歷史價格數據
  deleteHistoricalData: async (stockId: number): Promise<string> => {
    const response = await api.delete<string>(`/stock-prices/${stockId}`);
    return response.data;
  },

  // 取得支援的時間範圍
  getSupportedPeriods: async (): Promise<string[]> => {
    const response = await api.get<string[]>('/stock-prices/supported-periods');
    return response.data;
  },

  // 從 Yahoo Finance 取得真實歷史價格數據
  getRealStockPricesFromYahoo: async (symbol: string, period: string): Promise<StockPrice[]> => {
    const response = await api.get<StockPrice[]>(`/stock-prices/yahoo/${symbol}/period/${period}`);
    return response.data;
  },

  // 從 Alpha Vantage 取得真實歷史價格數據（僅支援日線）
  getRealStockPricesFromAlphaVantage: async (symbol: string, apiKey: string): Promise<StockPrice[]> => {
    const response = await api.get<StockPrice[]>(`/stock-prices/alphavantage/${symbol}?apiKey=${apiKey}`);
    return response.data;
  },
};

// 觀察清單相關 API
export const watchlistApi = {
  // 取得觀察清單
  getWatchlist: async (): Promise<WatchlistItem[]> => {
    const response = await api.get<WatchlistItem[]>('/watchlist');
    return response.data;
  },

  // 新增股票到觀察清單
  addToWatchlist: async (stockId: number): Promise<WatchlistItem> => {
    const request: AddToWatchlistRequest = { stockId };
    const response = await api.post<WatchlistItem>('/watchlist', request);
    return response.data;
  },

  // 從觀察清單移除股票
  removeFromWatchlist: async (stockId: number): Promise<void> => {
    await api.delete(`/watchlist/${stockId}`);
  },

  // 檢查股票是否在觀察清單中
  checkInWatchlist: async (stockId: number): Promise<boolean> => {
    const response = await api.get<CheckWatchlistResponse>(`/watchlist/check/${stockId}`);
    return response.data.inWatchlist;
  },
};

export default api; 