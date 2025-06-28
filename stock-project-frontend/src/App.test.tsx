import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import App from './App'

describe('App', () => {
  it('renders without crashing', () => {
    render(
      <BrowserRouter>
        <App />
      </BrowserRouter>
    )
    
    // 檢查主要元素是否存在
    expect(screen.getByText('股票市場')).toBeInTheDocument()
    expect(screen.getByText('首頁')).toBeInTheDocument()
    expect(screen.getByText('觀察清單')).toBeInTheDocument()
  })

  it('displays welcome message', () => {
    render(
      <BrowserRouter>
        <App />
      </BrowserRouter>
    )
    
    expect(screen.getByText('歡迎，訪客用戶')).toBeInTheDocument()
  })
}) 