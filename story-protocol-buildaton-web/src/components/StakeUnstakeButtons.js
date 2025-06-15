import React, { useState } from 'react';
import { stakeToAsset, unstakeFromAsset, connectWallet } from '../utils/web3Utils';

const StakeUnstakeButtons = ({ asset, onDataRefresh }) => {
  const [userAccount, setUserAccount] = useState(null);
  const [stakeAmount, setStakeAmount] = useState('');
  const [unstakeAmount, setUnstakeAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [showStakeInput, setShowStakeInput] = useState(false);
  const [showUnstakeInput, setShowUnstakeInput] = useState(false);

  const ensureWalletConnection = async () => {
    if (!userAccount) {
      try {
        const account = await connectWallet();
        setUserAccount(account);
        return account;
      } catch (error) {
        alert(error.message);
        return null;
      }
    }
    return userAccount;
  };

  const handleStake = async () => {
    const account = await ensureWalletConnection();
    if (!account) return;

    if (!stakeAmount || parseFloat(stakeAmount) <= 0) {
      alert('Please enter the amount to stake.');
      return;
    }

    setLoading(true);
    try {
      await stakeToAsset(asset.ipId, stakeAmount, account);
      const shouldRefresh = window.confirm('Staking completed successfully! Would you like to refresh the page?');
      setStakeAmount('');
      setShowStakeInput(false);
      
      if (shouldRefresh) {
        window.location.reload();
      } else {

        if (onDataRefresh) {
          onDataRefresh();
        }
      }
    } catch (error) {
      console.error('Staking failed:', error);
      alert('Staking failed: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleUnstake = async () => {
    const account = await ensureWalletConnection();
    if (!account) return;

    if (!unstakeAmount || parseFloat(unstakeAmount) <= 0) {
      alert('Please enter the amount to unstake.');
      return;
    }

    setLoading(true);
    try {
      await unstakeFromAsset(asset.ipId, unstakeAmount, account);
      const shouldRefresh = window.confirm('Unstaking completed successfully! Would you like to refresh the page?');
      setUnstakeAmount('');
      setShowUnstakeInput(false);
      
      if (shouldRefresh) {
        window.location.reload();
      } else {

        if (onDataRefresh) {
          onDataRefresh();
        }
      }
    } catch (error) {
      console.error('Unstaking failed:', error);
      alert('Unstaking failed: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="stake-unstake-container">

      <div className="action-buttons">
        <div className="stake-section">
          {!showStakeInput ? (
            <button 
              onClick={() => setShowStakeInput(true)}
              className="action-btn stake-btn"
              disabled={loading}
            >
              💰 Stake
            </button>
          ) : (
            <div className="input-section">
              <input
                type="number"
                placeholder="ETH Amount"
                value={stakeAmount}
                onChange={(e) => setStakeAmount(e.target.value)}
                className="amount-input"
                step="0.0001"
                min="0"
              />
              <div className="input-buttons">
                <button 
                  onClick={handleStake}
                  className="confirm-btn"
                  disabled={loading}
                >
                  {loading ? 'Processing...' : 'Confirm'}
                </button>
                <button 
                  onClick={() => {
                    setShowStakeInput(false);
                    setStakeAmount('');
                  }}
                  className="cancel-btn"
                  disabled={loading}
                >
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>

        <div className="unstake-section">
          {!showUnstakeInput ? (
            <button 
              onClick={() => setShowUnstakeInput(true)}
              className="action-btn unstake-btn"
              disabled={loading}
            >
              💸 Unstake
            </button>
          ) : (
            <div className="input-section">
              <input
                type="number"
                placeholder="ETH Amount"
                value={unstakeAmount}
                onChange={(e) => setUnstakeAmount(e.target.value)}
                className="amount-input"
                step="0.0001"
                min="0"
              />
              <div className="input-buttons">
                <button 
                  onClick={handleUnstake}
                  className="confirm-btn"
                  disabled={loading}
                >
                  {loading ? 'Processing...' : 'Confirm'}
                </button>
                <button 
                  onClick={() => {
                    setShowUnstakeInput(false);
                    setUnstakeAmount('');
                  }}
                  className="cancel-btn"
                  disabled={loading}
                >
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default StakeUnstakeButtons;
