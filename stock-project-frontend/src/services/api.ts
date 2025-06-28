import axios from 'axios';
import { Stock, WatchlistItem, AddToWatchlistRequest, CheckWatchlistResponse } from '../types';

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