import React, { useState, useEffect } from 'react';
import { ethers } from 'ethers';
import './SlashHistorySection.css';

const SlashHistorySection = () => {
  const [slashHistory, setSlashHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [hasFetched, setHasFetched] = useState(false);

  const fetchSlashHistory = async () => {
    if (loading) return; // 이미 로딩 중이면 중복 호출 방지
    
    setLoading(true);
    setError(null);
    
    try {
      const response = await fetch('/api/v1/slash-history?offset=9&limit=10', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setSlashHistory(data.records || []);
      setHasFetched(true);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching slash history:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!hasFetched) {
      fetchSlashHistory();
    }
  }, [hasFetched]);

  const formatAddress = (address) => {
    if (!address) return 'N/A';
    try {
      // ethers.getAddress를 사용하여 체크섬 형식으로 변환
      return ethers.getAddress(address);
    } catch (error) {
      console.error('Invalid address format:', address, error);
      return address; // 변환 실패시 원본 주소 반환
    }
  };



  const formatAmount = (amount) => {

    const amountInEther = parseFloat(amount) / Math.pow(10, 18);
    return amountInEther.toFixed(4);
  };

  const formatTimestamp = (timestamp) => {
    const date = new Date(timestamp);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffDays > 0) {
      return `${diffDays}d ago`;
    } else if (diffHours > 0) {
      return `${diffHours}h ago`;
    } else if (diffMins > 0) {
      return `${diffMins}m ago`;
    } else {
      return 'Just now';
    }
  };

  if (loading) {
    return (
      <div className="slash-history-section">
        <div className="slash-history-header">
          <h3>Recent Slash History</h3>
        </div>
        <div className="slash-history-loading">
          <div className="loading-spinner"></div>
          <span>Loading slash history...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="slash-history-section">
        <div className="slash-history-header">
          <h3>Recent Slash History</h3>
        </div>
        <div className="slash-history-error">
          <span className="error-icon">⚠️</span>
          <span>Failed to load slash history</span>
          <button onClick={fetchSlashHistory} className="retry-btn">
            Retry
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="slash-history-section">
      <div className="slash-history-header">
        <h3>Recent Slash History</h3>
        <button onClick={fetchSlashHistory} className="refresh-history-btn">
          🔄
        </button>
      </div>
      
      {slashHistory.length === 0 ? (
        <div className="slash-history-empty">
          <span className="empty-icon">📜</span>
          <span>No slash history available</span>
        </div>
      ) : (
        <div className="slash-history-list">
          {slashHistory.map((record, index) => (
            <div key={index} className="slash-history-item">
              <div className="slash-record-main">
                <div className="slash-main-info">
                  <div className="slash-addresses">
                    <span className="slashed-asset">
                      <span className="asset-label">Slashed:</span>
                      <span className="asset-address">{formatAddress(record.slashedIPAsset)}</span>
                    </span>
                    <span className="arrow">→</span>
                    <span className="redistributed-asset">
                      <span className="asset-label">To:</span>
                      <span className="asset-address">{formatAddress(record.redistributedToIPAsset)}</span>
                    </span>
                  </div>
                  <div className="slash-amount">
                    <span className="amount-icon">💰</span>
                    <span className="amount-value">{formatAmount(record.amount)}</span>
                    <span className="amount-unit">ETH</span>
                  </div>
                </div>
                <div className="slash-time">
                  <span className="time-icon">🕐</span>
                  {formatTimestamp(record.timestamp)}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SlashHistorySection;
