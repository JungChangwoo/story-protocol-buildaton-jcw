# Smart Contract - IP Staking Module

## Overview

This directory contains the core smart contract for the Story IP Trust Staking system.

## Contract: `IPStakingModule.sol`

### Key Features
- Stake ETH on IP assets
- Unstake with amount validation
- Slash malicious IP assets
- Automatic redistribution of slashed stakes
- Comprehensive slash history tracking

### Deployment

```bash
# Deploy to testnet
# Update deployment script with your configuration
```

### Contract Address
- **Testnet**: `0x6E0D112252335D08DBFCD1dA187Bcc43cdA62a9B` (example)

### Main Functions

```solidity
stake(address ipAssetId) payable     // Stake ETH on IP asset
unstake(address ipAssetId, uint256)  // Unstake specific amount
slash(address ipAssetId) onlyOwner   // Slash IP asset
getTotalStakedAmount(address)        // View total stakes
getSlashHistoryPaginated(uint256, uint256) // Get slash history
```

### Events

- `Staked(address user, address ipAssetId, uint256 amount)`
- `Unstaked(address user, address ipAssetId, uint256 amount)`
- `Slashed(address ipAssetId, uint256 totalSlashedAmount)`
- `SlashRedistributed(address fromIPAsset, address toIPAsset, uint256 amount)`

### Security Features

- OpenZeppelin Ownable for access control
- Reentrancy protection
- Input validation
- Gas optimization with batch functions

---

📄 **License**: MIT  
🔗 **Main Project**: [../README.md](../README.md)
