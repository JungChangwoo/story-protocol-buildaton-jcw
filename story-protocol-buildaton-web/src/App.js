import React, { useState, useEffect, useMemo } from 'react';
import './App.css';
import IPAssetList from './components/IPAssetList';
import LoadingSpinner from './components/LoadingSpinner';
import ErrorMessage from './components/ErrorMessage';
import SearchAndFilter from './components/SearchAndFilter';
import SlashHistorySection from './components/SlashHistorySection';
import { connectWallet, checkMetaMask } from './utils/web3Utils';

function App() {
  const [ipAssets, setIpAssets] = useState([]);
  const [loading, setLoading] = useState(false);
  const [backgroundLoading, setBackgroundLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [trustRankFilter, setTrustRankFilter] = useState('ALL');
  const [userAddress, setUserAddress] = useState(null);
  const [walletCheckCompleted, setWalletCheckCompleted] = useState(false);
  const [selectedAsset, setSelectedAsset] = useState(null);
  const [sidePanelOpen, setSidePanelOpen] = useState(false);
  const [walletConnecting, setWalletConnecting] = useState(false);
  const [isInitialLoad, setIsInitialLoad] = useState(true);
  const [filters, setFilters] = useState({
    orderBy: 'blocknumber',
    orderDirection: 'desc',
    limit: 20
  });

  const fetchIPAssets = async (onComplete = null, isBackgroundRefresh = false) => {
    // 이미 로딩 중이면 중복 호출 방지
    if (loading && !isBackgroundRefresh) return;
    if (backgroundLoading && isBackgroundRefresh) return;
    
    if (isBackgroundRefresh) {
      setBackgroundLoading(true);
    } else {
      setLoading(true);
    }
    setError(null);
    
    try {
      const requestBody = {
        options: {
          orderBy: filters.orderBy,
          orderDirection: filters.orderDirection,
          pagination: {
            limit: filters.limit
          }
        }
      };


      if (searchTerm && searchTerm.trim()) {
        requestBody.options.ipAssetIds = [searchTerm.trim()];
      }


      if (userAddress) {
        requestBody.userAddress = userAddress;
      }

      const response = await fetch('/api/v3/assets', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setIpAssets(data.data || []);
      setIsInitialLoad(false);
      

      if (onComplete) {
        onComplete();
      }
    } catch (err) {
      setError(err.message);
      console.error('Error fetching IP Assets:', err);
    } finally {
      if (isBackgroundRefresh) {
        setBackgroundLoading(false);
      } else {
        setLoading(false);
      }
    }
  };


  const filteredAssets = useMemo(() => {
    let filtered = ipAssets;
    

    if (trustRankFilter !== 'ALL') {
      filtered = filtered.filter(asset => {
        const assetTrustRank = asset.trustRank || 'BRONZE';
        return assetTrustRank === trustRankFilter;
      });
    }
    
    return filtered;
  }, [ipAssets, trustRankFilter]);


  const sortedAndFilteredAssets = useMemo(() => {
    const sorted = [...filteredAssets];
    
    sorted.sort((a, b) => {
      let aValue, bValue;
      

      aValue = parseInt(a.blockNumber || '0');
      bValue = parseInt(b.blockNumber || '0');
      
      if (filters.orderDirection === 'asc') {
        return aValue - bValue;
      } else {
        return bValue - aValue;
      }
    });
    
    return sorted;
  }, [filteredAssets, filters]);


  const handleConnectWallet = async () => {
    setWalletConnecting(true);
    try {
      const address = await connectWallet();
      setUserAddress(address);
      console.log('Wallet connected:', address);

      fetchIPAssets();
    } catch (error) {
      console.error('Wallet connection failed:', error);
      setError('Failed to connect to MetaMask.');
    } finally {
      setWalletConnecting(false);
    }
  };


  const handleDisconnectWallet = () => {
    setUserAddress(null);

    fetchIPAssets();
  };


  useEffect(() => {
    if (window.ethereum) {
      const handleAccountsChanged = (accounts) => {
        if (accounts.length === 0) {
          setUserAddress(null);
        } else {
          setUserAddress(accounts[0]);
        }

        fetchIPAssets();
      };

      window.ethereum.on('accountsChanged', handleAccountsChanged);


      return () => {
        if (window.ethereum) {
          window.ethereum.removeListener('accountsChanged', handleAccountsChanged);
        }
      };
    }
  }, []);


  useEffect(() => {
    const initializeApp = async () => {
      if (window.ethereum) {
        try {
          const accounts = await window.ethereum.request({ method: 'eth_accounts' });
          if (accounts.length > 0) {
            setUserAddress(accounts[0]);
          }
        } catch (error) {
          console.error('Failed to check existing connection:', error);
        }
      }
      

      setWalletCheckCompleted(true);
    };

    initializeApp();
  }, []);


  // 데이터 fetch를 위한 통합된 useEffect
  useEffect(() => {
    if (walletCheckCompleted) {
      fetchIPAssets();
    }
  }, [walletCheckCompleted, filters.orderBy, filters.orderDirection, filters.limit, searchTerm]);

  const handleSearch = (term) => {
    setSearchTerm(term);
  };

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
  };

  const handleTrustRankFilter = (rank) => {
    setTrustRankFilter(rank);
  };

  const handleAssetClick = (asset) => {
    setSelectedAsset(asset);
    setSidePanelOpen(true);
  };

  const handleCloseSidePanel = () => {
    setSidePanelOpen(false);
    setTimeout(() => setSelectedAsset(null), 300);
  };


  const handleDataRefreshWithClose = () => {
    fetchIPAssets(() => {

      handleCloseSidePanel();
    }, true);
  };


  const trustRankStats = useMemo(() => {
    const stats = {
      DIAMOND: 0,
      PLATINUM: 0,
      GOLD: 0,
      SILVER: 0,
      BRONZE: 0
    };
    
    ipAssets.forEach(asset => {
      const rank = asset.trustRank || 'BRONZE';
      if (stats.hasOwnProperty(rank)) {
        stats[rank] += 1;
      }
    });
    
    return stats;
  }, [ipAssets]);

  return (
    <div className="App">
      <header className="App-header">
        <div className="header-content">
          <div className="brand-section">
            <h1>
              <span className="brand-icon">⭐</span>
              <span className="brand-name">Story IP Trust Staking</span>
            </h1>
            <p className="brand-description">
              Rate, stake, and discover trusted IP Assets on Story Protocol
            </p>
          </div>
        </div>
        <div className="header-actions">

          <div className="wallet-section">
            {userAddress ? (
              <div className="wallet-connected">
                <span className="wallet-address">
                  👤 {userAddress.slice(0, 6)}...{userAddress.slice(-4)}
                </span>
                <button 
                  onClick={handleDisconnectWallet} 
                  className="disconnect-btn"
                >
                  Disconnect
                </button>
              </div>
            ) : (
              <button 
                onClick={handleConnectWallet} 
                disabled={walletConnecting || !checkMetaMask()}
                className="connect-wallet-btn"
              >
                {walletConnecting ? 'Connecting...' : 'Connect Wallet'}
              </button>
            )}
          </div>
          
          <button onClick={fetchIPAssets} disabled={!walletCheckCompleted || loading || backgroundLoading} className="refresh-btn">
            {!walletCheckCompleted ? 'Initializing...' : loading ? 'Loading...' : backgroundLoading ? 'Updating...' : 'Refresh'}
          </button>
        </div>
      </header>
      
      <main className="App-main">
        <SlashHistorySection />
        
        <SearchAndFilter
          onSearch={handleSearch}
          onFilterChange={handleFilterChange}
          onTrustRankFilter={handleTrustRankFilter}
          filters={filters}
          searchTerm={searchTerm}
          trustRankFilter={trustRankFilter}
        />
        
        {(!walletCheckCompleted || loading) && <LoadingSpinner />}
        {walletCheckCompleted && error && <ErrorMessage message={error} onRetry={fetchIPAssets} />}
        {walletCheckCompleted && !loading && !error && (
          <IPAssetList 
            assets={sortedAndFilteredAssets} 
            totalCount={ipAssets.length}
            filteredCount={sortedAndFilteredAssets.length}
            searchTerm={searchTerm}
            trustRankFilter={trustRankFilter}
            trustRankStats={trustRankStats}
            onTrustRankFilter={handleTrustRankFilter}
            userAddress={userAddress}
            selectedAsset={selectedAsset}
            onAssetSelect={handleAssetClick}
            onAssetClose={handleCloseSidePanel}
            onDataRefresh={handleDataRefreshWithClose}
          />
        )}
      </main>
    </div>
  );
}

export default App;
