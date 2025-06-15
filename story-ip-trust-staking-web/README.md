# Frontend Web Application - Story IP Trust Staking

## Overview

React-based web application providing user interface for the Story IP Trust Staking system. Users can stake ETH on IP assets, view staking information, and monitor slash history.

## Tech Stack

- **React 19.1.0**
- **Ethers.js 6.0** for Web3 integration
- **Modern JavaScript (ES2022+)**
- **CSS3** with responsive design

## Quick Start

### Prerequisites
- Node.js 18+
- MetaMask or compatible Web3 wallet
- Backend server running on `http://localhost:8080`

### Installation & Setup

```bash
# Install dependencies
npm install

# Start development server
npm start
```

The application will be available at `http://localhost:3000`

### Build for Production

```bash
# Create production build
npm run build

# Serve production build locally
npx serve -s build
```

## Features

### Core Functionality

- **🔐 Wallet Connection**: Connect MetaMask and other Web3 wallets
- **💰 IP Asset Staking**: Stake ETH on Story Protocol IP assets
- **📤 Unstaking**: Withdraw staked amounts
- **📊 Dashboard**: View staking statistics and balances
- **📋 Slash History**: Monitor slash events and redistributions
- **📱 Responsive Design**: Mobile-friendly interface

### User Interface

- Clean, modern design with intuitive navigation
- Real-time balance updates
- Transaction status indicators
- Error handling with user-friendly messages
- Loading states for blockchain operations

## Configuration

### Environment Variables

Create a `.env` file in the root directory:

```bash
# Backend API URL
REACT_APP_API_URL=http://localhost:8080

# Smart Contract Address
REACT_APP_CONTRACT_ADDRESS=0x6E0D112252335D08DBFCD1dA187Bcc43cdA62a9B

# Network Configuration
REACT_APP_NETWORK_NAME=sepolia
REACT_APP_CHAIN_ID=11155111
```

### Wallet Configuration

The application automatically detects and connects to:
- MetaMask
- WalletConnect compatible wallets
- Other injected Web3 providers

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── WalletConnect.js
│   ├── StakingForm.js
│   ├── StakingHistory.js
│   └── SlashHistory.js
├── hooks/              # Custom React hooks
│   ├── useWallet.js
│   ├── useContract.js
│   └── useStaking.js
├── services/           # API and blockchain services
│   ├── api.js
│   ├── contract.js
│   └── web3.js
├── utils/              # Utility functions
│   ├── format.js
│   └── validation.js
├── styles/             # CSS stylesheets
└── App.js             # Main application component
```

## Key Components

### Wallet Integration

```javascript
// Connect wallet and handle account changes
const { account, connect, disconnect } = useWallet();

// Connect to MetaMask
await connect();
```

### Staking Operations

```javascript
// Stake ETH on IP asset
const stakeAmount = ethers.parseEther("1.0");
await stakingContract.stake(ipAssetAddress, { value: stakeAmount });

// Unstake from IP asset
await stakingContract.unstake(ipAssetAddress, unstakeAmount);
```

### Real-time Data

```javascript
// Listen to contract events
useEffect(() => {
  const filter = contract.filters.Staked();
  contract.on(filter, handleStakeEvent);
  
  return () => contract.off(filter, handleStakeEvent);
}, [contract]);
```

## Testing

```bash
# Run test suite
npm test

# Run tests with coverage
npm test -- --coverage

# Run tests in watch mode
npm test -- --watch
```

## Deployment

### Development
```bash
npm start
```

### Production
```bash
npm run build
# Deploy build/ directory to your hosting service
```

### Proxy Configuration

The application is configured to proxy API requests to the backend server:
```json
{
  "proxy": "http://localhost:8080"
}
```

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Troubleshooting

### Common Issues

1. **MetaMask Connection Issues**
   - Ensure MetaMask is installed and unlocked
   - Check network configuration
   - Clear browser cache if needed

2. **Transaction Failures**
   - Verify sufficient ETH balance for gas
   - Check contract address configuration
   - Ensure correct network connection

3. **API Connection Issues**
   - Verify backend server is running
   - Check proxy configuration
   - Verify CORS settings

---

📄 **License**: MIT  
🔗 **Main Project**: [../../README.md](../../README.md)  
🌐 **Live Demo**: [Demo Link]
