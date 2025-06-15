import React, { useState } from 'react';
import { getTrustRankConfig, formatTrustRankValue } from '../utils/trustRankUtils';
import StakeUnstakeButtons from './StakeUnstakeButtons';
import { ethers } from 'ethers';
import { slashAsset, connectWallet } from '../utils/web3Utils';

const IPAssetSidePanel = ({ asset, userAddress, isOpen, onClose, onDataRefresh }) => {
  const [imageError, setImageError] = useState(false);
  const [slashLoading, setSlashLoading] = useState(false);
  const [copied, setCopied] = useState(false);
  

  const OWNER_ADDRESS = '0x81dc16A2A3026D1bbaFc021c7a8bC15D1a357D02';
  

  const isOwner = userAddress && userAddress.toLowerCase() === OWNER_ADDRESS.toLowerCase();
  
  if (!asset || !isOpen) return null;

  const assetName = asset.nftMetadata?.name || `Asset #${asset.id.slice(-8)}`;
  const imageUrl = asset.nftMetadata?.imageUrl;
  const trustRank = formatTrustRankValue(asset.trustRank);
  const trustRankConfig = getTrustRankConfig(trustRank);

  const handleImageError = () => {
    setImageError(true);
  };

  const copyToClipboard = async (text) => {
    try {
      await navigator.clipboard.writeText(text);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy text: ', err);
    }
  };


  const formatEthAmount = (weiAmount) => {
    if (!weiAmount || weiAmount === '0') return '0';
    try {
      const ethAmount = ethers.formatEther(weiAmount);
      return parseFloat(ethAmount).toFixed(6).replace(/\.?0+$/, '');
    } catch (error) {
      console.error('ETH conversion error:', error);
      return '0';
    }
  };

  const formatAddress = (address) => {
    if (!address) return 'N/A';
    return `${address.slice(0, 6)}...${address.slice(-4)}`;
  };

  const formatTimestamp = (timestamp) => {
    if (!timestamp) return 'N/A';
    return new Date(timestamp * 1000).toLocaleDateString();
  };

  const handleSlash = async () => {
    if (!userAddress) {
      try {
        await connectWallet();
      } catch (error) {
        alert(error.message);
        return;
      }
    }

    if (!window.confirm('Are you sure you want to slash this asset? This action cannot be undone.')) {
      return;
    }

    setSlashLoading(true);
    try {
      await slashAsset(asset.ipId);
      const shouldRefresh = window.confirm('Slash completed successfully! Would you like to refresh the page?');
      
      if (shouldRefresh) {
        window.location.reload();
      } else {
        if (onDataRefresh) {
          onDataRefresh();
        }
      }
    } catch (error) {
      console.error('Slash failed:', error);
      alert('Slash failed: ' + error.message);
    } finally {
      setSlashLoading(false);
    }
  };

  return (
    <>

      <div className="side-panel-backdrop" onClick={onClose} />
      

      <div className="side-panel">
        <div className="side-panel-header">
          <h2>{assetName}</h2>
          <button className="close-btn" onClick={onClose}>
            ✕
          </button>
        </div>
        
        <div className="side-panel-content">

          <div className="detail-image-container">
            {imageUrl && !imageError ? (
              <img 
                src={imageUrl} 
                alt={assetName}
                className="detail-asset-image"
                onError={handleImageError}
              />
            ) : (
              <div className="detail-asset-placeholder">
                🖼️
              </div>
            )}
            

            <div className="detail-trust-rank-badge" style={{ background: trustRankConfig.gradient }}>
              <span className="trust-rank-badge-emoji">{trustRankConfig.emoji}</span>
              <span className="trust-rank-badge-text">{trustRankConfig.name}</span>
            </div>
          </div>


          <div className="detail-metadata">
            <div className="detail-section">
              <h4>Basic Information</h4>
              <div className="detail-grid">
                <div className="detail-item ip-id-item">
                  <span className="detail-label">IP ID:</span>
                  <div className="ip-id-container">
                    <span className="detail-value ip-id-full">{asset.ipId}</span>
                    <button 
                      className="copy-btn"
                      onClick={() => copyToClipboard(asset.ipId)}
                      title="Copy IP ID"
                    >
                      {copied ? '✓' : '📋'}
                    </button>
                  </div>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Block Number:</span>
                  <span className="detail-value">{asset.blockNumber || 'N/A'}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Created:</span>
                  <span className="detail-value">{formatTimestamp(asset.blockTimestamp)}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Children Count:</span>
                  <span className="detail-value">
                    <span className="children-count">{asset.childrenCount || 0}</span>
                  </span>
                </div>
                {asset.nftMetadata?.tokenContract && (
                  <div className="detail-item">
                    <span className="detail-label">Contract:</span>
                    <span className="detail-value">{formatAddress(asset.nftMetadata.tokenContract)}</span>
                  </div>
                )}
                {asset.nftMetadata?.tokenUri && (
                  <div className="detail-item full-width">
                    <span className="detail-label">Token URL:</span>
                    <a 
                      href={asset.nftMetadata.tokenUri} 
                      target="_blank" 
                      rel="noopener noreferrer"
                      className="detail-token-url-link"
                    >
                      View Token ↗
                    </a>
                  </div>
                )}
              </div>
            </div>


            {asset.stakingInfo && (
              <div className="detail-section">
                <h4>Staking Information</h4>
                <div className="detail-staking-info">
                  <div className="detail-staking-item">
                    <span className="detail-staking-label">Total Staked:</span>
                    <span className="detail-staking-value">
                      {formatEthAmount(asset.stakingInfo.totalStakedAmount)} ETH
                    </span>
                  </div>
                  
                  <div className="detail-staking-item user-stake-detail">
                    <span className="detail-staking-label">Your Stake:</span>
                    {userAddress ? (
                      <span className="detail-staking-value">
                        {formatEthAmount(asset.stakingInfo.userStakedAmount || '0')} ETH
                      </span>
                    ) : (
                      <span className="detail-connect-message">
                        Connect wallet to view
                      </span>
                    )}
                  </div>
                </div>
              </div>
            )}


            {asset.stakingInfo?.slashHistory && asset.stakingInfo.slashHistory.length > 0 && (
              <div className="detail-section">
                <h4>Slash History</h4>
                <div className="detail-slash-history">
                  {asset.stakingInfo.slashHistory.map((record, index) => (
                    <div key={index} className="detail-slash-record">
                      <div className="slash-record-header">
                        <span className="slash-record-icon">⚔️</span>
                        <span className="slash-record-date">
                          {new Date(record.timestamp * 1000).toLocaleDateString()}
                        </span>
                      </div>
                      <div className="slash-record-details">
                        <div className="slash-record-flow">
                          <div className="slash-participant">
                            <span className="participant-label">Slashed:</span>
                            <span className="participant-address">{formatAddress(record.slashedIPAsset)}</span>
                          </div>
                          <span className="slash-arrow">→</span>
                          <div className="slash-participant">
                            <span className="participant-label">To:</span>
                            <span className="participant-address">{formatAddress(record.redistributedToIPAsset)}</span>
                          </div>
                        </div>
                        <div className="slash-amount-detail">
                          <span className="amount-icon">💰</span>
                          <span className="amount-value">{formatEthAmount(record.amount.toString())}</span>
                          <span className="amount-unit">ETH</span>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}


            <div className="detail-section">
              <h4>Actions</h4>
              <div className="action-buttons-container">
                <StakeUnstakeButtons asset={asset} onDataRefresh={onDataRefresh} />
                

                {isOwner && (
                  <div className="slash-section">
                    <button 
                      onClick={handleSlash}
                      className="action-btn slash-btn"
                      disabled={slashLoading}
                    >
                      {slashLoading ? 'Processing...' : '⚔️ Slash'}
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default IPAssetSidePanel;
