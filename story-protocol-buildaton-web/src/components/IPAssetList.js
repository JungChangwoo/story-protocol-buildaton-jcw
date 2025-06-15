import React from 'react';
import IPAssetCard from './IPAssetCard';
import IPAssetSidePanel from './IPAssetSidePanel';
import { getTrustRankConfig } from '../utils/trustRankUtils';

const IPAssetList = ({ assets, totalCount, filteredCount, searchTerm, trustRankFilter, trustRankStats, onTrustRankFilter, userAddress, selectedAsset, onAssetSelect, onAssetClose, onDataRefresh }) => {
  const showingFiltered = searchTerm || trustRankFilter !== 'ALL';
  const showingFilteredText = searchTerm && trustRankFilter !== 'ALL' 
    ? `IP ID "${searchTerm}" and ${getTrustRankConfig(trustRankFilter).name} rank`
    : searchTerm 
    ? `IP ID "${searchTerm}"`
    : `${getTrustRankConfig(trustRankFilter).name} rank`;
  
  const handleStatsItemClick = (rank) => {

    if (trustRankFilter === rank) {
      onTrustRankFilter('ALL');
    } else {
      onTrustRankFilter(rank);
    }
  };
  
  if (!assets || assets.length === 0) {
    if (showingFiltered) {
      return (
        <div className="no-assets">
          <h3>No IP Assets Found</h3>
          <p>No assets match your filters for {showingFilteredText}. Try adjusting your search or filters.</p>
        </div>
      );
    }
    
    return (
      <div className="no-assets">
        <h3>No IP Assets Found</h3>
        <p>There are currently no IP assets to display. Try refreshing or check back later.</p>
      </div>
    );
  }

  return (
    <div className="ip-assets-container">
      <div className="assets-header">
        <h2>
          IP Assets 
          {showingFiltered ? (
            <span className="asset-count">
              ({filteredCount} of {totalCount} assets)
            </span>
          ) : (
            <span className="asset-count">({assets.length})</span>
          )}
        </h2>
        
        {showingFiltered && (
          <p className="search-info">
            Showing results for {showingFilteredText}
          </p>
        )}
        

        {totalCount > 0 && (
          <div className="trust-rank-stats">
            <h3>
              Trust Rank Distribution 
              <span className="stats-subtitle">Click to filter</span>
            </h3>
            <div className="stats-grid">
              {Object.entries(trustRankStats).map(([rank, count]) => {
                const config = getTrustRankConfig(rank);
                const percentage = totalCount > 0 ? ((count / totalCount) * 100).toFixed(1) : 0;
                const isActive = trustRankFilter === rank;
                
                return (
                  <div 
                    key={rank} 
                    className={`stats-item ${isActive ? 'stats-item-active' : ''} ${count === 0 ? 'stats-item-disabled' : ''}`}
                    style={{ 
                      borderColor: config.color,
                      background: isActive ? config.gradient : 'white'
                    }}
                    onClick={() => count > 0 && handleStatsItemClick(rank)}
                    title={`${count} ${config.name} ranked assets (${percentage}%). Click to ${isActive ? 'show all' : 'filter'}.`}
                  >
                    <div className="stats-emoji">{config.emoji}</div>
                    <div className="stats-info">
                      <div className={`stats-name ${isActive ? 'stats-name-active' : ''}`}>
                        {config.name}
                      </div>
                      <div className={`stats-count ${isActive ? 'stats-count-active' : ''}`}>
                        {count} ({percentage}%)
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}
      </div>
      
      <div className="ip-assets-grid">
        {assets.map((asset) => (
          <IPAssetCard 
            key={asset.id} 
            asset={asset}
            searchTerm={searchTerm}
            userAddress={userAddress}
            onAssetClick={onAssetSelect}
          />
        ))}
      </div>
      

      <IPAssetSidePanel
        asset={selectedAsset}
        userAddress={userAddress}
        isOpen={!!selectedAsset}
        onClose={onAssetClose}
        onDataRefresh={onDataRefresh}
      />
    </div>
  );
};

export default IPAssetList;
