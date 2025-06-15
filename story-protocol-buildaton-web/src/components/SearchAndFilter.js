import React, { useState, useEffect } from 'react';
import { getTrustRankConfig } from '../utils/trustRankUtils';

const SearchAndFilter = ({ onSearch, onFilterChange, filters, searchTerm, onTrustRankFilter, trustRankFilter }) => {
  const [localSearchTerm, setLocalSearchTerm] = useState(searchTerm);
  const trustRanks = ['ALL', 'DIAMOND', 'PLATINUM', 'GOLD', 'SILVER', 'BRONZE'];
  

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      onSearch(localSearchTerm);
    }, 500);

    return () => clearTimeout(timeoutId);
  }, [localSearchTerm, onSearch]);


  useEffect(() => {
    setLocalSearchTerm(searchTerm);
  }, [searchTerm]);
  
  return (
    <div className="search-filter-container">
      <div className="search-section">
        <div className="search-input-container">
          <input
            type="text"
            placeholder="Search by IP ID..."
            value={localSearchTerm}
            onChange={(e) => setLocalSearchTerm(e.target.value)}
            className="search-input"
          />
          <span className="search-icon">🔍</span>
        </div>
      </div>
      
      <div className="filter-section">
        <div className="filter-group">
          <label htmlFor="trustRankFilter">Trust Rank:</label>
          <select
            id="trustRankFilter"
            value={trustRankFilter}
            onChange={(e) => onTrustRankFilter(e.target.value)}
            className="filter-select trust-rank-filter"
          >
            {trustRanks.map(rank => {
              if (rank === 'ALL') {
                return <option key={rank} value={rank}>All Ranks</option>;
              }
              const config = getTrustRankConfig(rank);
              return (
                <option key={rank} value={rank}>
                  {config.emoji} {config.name}
                </option>
              );
            })}
          </select>
        </div>
        
        <div className="filter-group">
          <label htmlFor="sortBy">Sort by:</label>
          <select
            id="sortBy"
            value={filters.orderBy}
            onChange={(e) => onFilterChange({ ...filters, orderBy: e.target.value })}
            className="filter-select"
          >
            <option value="blocknumber">Block Number</option>
          </select>
        </div>
        
        <div className="filter-group">
          <label htmlFor="sortDirection">Direction:</label>
          <select
            id="sortDirection"
            value={filters.orderDirection}
            onChange={(e) => onFilterChange({ ...filters, orderDirection: e.target.value })}
            className="filter-select"
          >
            <option value="desc">Descending</option>
            <option value="asc">Ascending</option>
          </select>
        </div>
        
        <div className="filter-group">
          <label htmlFor="limit">Items per page:</label>
          <select
            id="limit"
            value={filters.limit}
            onChange={(e) => onFilterChange({ ...filters, limit: parseInt(e.target.value) })}
            className="filter-select"
          >
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
          </select>
        </div>
      </div>
    </div>
  );
};

export default SearchAndFilter;
