import React, { useState } from 'react';

interface SearchBarProps {
  onSearch: (query: string) => void;
  placeholder?: string;
}

/**
 * 搜尋欄元件
 * 
 * 提供股票搜尋功能，支援即時搜尋和按鍵搜尋。
 * 
 * @param onSearch 搜尋回調函數
 * @param placeholder 搜尋框提示文字
 */
const SearchBar: React.FC<SearchBarProps> = ({ 
  onSearch, 
  placeholder = "搜尋股票代碼或名稱..." 
}) => {
  const [query, setQuery] = useState('');

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(query);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setQuery(value);
    // 即時搜尋（可選）
    onSearch(value);
  };

  return (
    <div className="w-full max-w-md">
      <form onSubmit={handleSearch} className="relative">
        <div className="relative">
          <input
            type="text"
            value={query}
            onChange={handleInputChange}
            placeholder={placeholder}
            className="input pl-10 pr-4"
          />
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <svg
              className="h-5 w-5 text-gray-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
          </div>
        </div>
        <button
          type="submit"
          className="absolute right-0 top-0 mt-0 mr-0 h-full px-4 bg-primary-600 text-white rounded-r-lg hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-primary-500"
        >
          搜尋
        </button>
      </form>
    </div>
  );
};

export default SearchBar; 