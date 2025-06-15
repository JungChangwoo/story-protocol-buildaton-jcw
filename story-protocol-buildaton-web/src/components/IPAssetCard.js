import React, { useState } from 'react';
import { getTrustRankConfig, formatTrustRankValue } from '../utils/trustRankUtils';
import StakeUnstakeButtons from './StakeUnstakeButtons';
import { ethers } from 'ethers';

const IPAssetCard = ({ asset, searchTerm, userAddress, onAssetClick }) => {
  const [imageError, setImageError] = useState(false);
  

  const assetName = asset.nftMetadata?.name || `Asset #${asset.id.slice(-8)}`;
  const imageUrl = asset.nftMetadata?.imageUrl;
  

  const trustRank = formatTrustRankValue(asset.trustRank);
  const trustRankConfig = getTrustRankConfig(trustRank);
  

  const highlightText = (text, search) => {
    if (!search || !text) return text;
    
    const regex = new RegExp(`(${search})`, 'gi');
    const parts = text.split(regex);
    
    return parts.map((part, index) => 
      regex.test(part) ? (
        <mark key={index} className="search-highlight">{part}</mark>
      ) : part
    );
  };

  const handleImageError = () => {
    setImageError(true);
  };

  const formatTimestamp = (timestamp) => {
    if (!timestamp) return 'N/A';
    return new Date(timestamp * 1000).toLocaleDateString();
  };


  const formatEthAmount = (weiAmount) => {
    if (!weiAmount || weiAmount === '0') return '0';
    try {
      const ethAmount = ethers.formatEther(weiAmount);

      return parseFloat(ethAmount).toFixed(6).replace(/\.?0+$/, '');
    } catch (error) {
      console.error('ETH 변환 오류:', error);
      return '0';
    }
  };

  const handleCardClick = () => {
    onAssetClick(asset);
  };

  return (
    <div 
      className={`ip-asset-card trust-rank-${trustRank.toLowerCase()}`}
      onClick={handleCardClick}
    >
      <div className="asset-image-container">
        {imageUrl && !imageError ? (
          <img 
            src={imageUrl} 
            alt={assetName}
            className="asset-image"
            onError={handleImageError}
          />
        ) : (
          <div className="asset-placeholder">
            🖼️
          </div>
        )}
        

        <div className="trust-rank-badge" style={{ background: trustRankConfig.gradient }}>
          <span className="trust-rank-badge-emoji">{trustRankConfig.emoji}</span>
          <span className="trust-rank-badge-text">{trustRankConfig.name}</span>
        </div>
      </div>
      
      <div className="asset-details">
        <h3>{highlightText(assetName, searchTerm)}</h3>
        
        <div className="asset-basic-info">
          <div className="info-item">
            <span className="info-label">Created:</span>
            <span className="info-value">{formatTimestamp(asset.blockTimestamp)}</span>
          </div>
          

          {asset.stakingInfo && (
            <div className="info-item staked-amount">
              <span className="info-label">Total Staked:</span>
              <span className="info-value staked-value">
                {formatEthAmount(asset.stakingInfo.totalStakedAmount)} ETH
              </span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default IPAssetCard;
