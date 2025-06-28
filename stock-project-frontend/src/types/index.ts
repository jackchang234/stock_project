// 股票資料類型
export interface Stock {
  id: number;
  symbol: string;
  name: string;
  price: number;
}

// 股票歷史價格資料類型
export interface StockPrice {
  id: number;
  stockId: number;
  symbol: string;
  date: string;
  openPrice: number;
  closePrice: number;
  highPrice: number;
  lowPrice: number;
  volume: number;
}

// 觀察清單項目類型
export interface WatchlistItem {
  id: number;
  stockId: number;
  stock: Stock;
}

// API 回應類型
export interface ApiResponse<T> {
  data?: T;
  error?: string;
}

// 搜尋請求類型
export interface SearchRequest {
  query: string;
}

// 新增到觀察清單請求類型
export interface AddToWatchlistRequest {
  stockId: number;
}

// 檢查觀察清單回應類型
export interface CheckWatchlistResponse {
  inWatchlist: boolean;
}

// 時間範圍類型
export type TimePeriod = '3M' | '1Y' | '2Y' | '3Y' | '5Y';

// 圖表數據類型
export interface ChartData {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
    borderColor: string;
    backgroundColor: string;
    tension: number;
  }[];
} 