# 🏆 Story IP Trust Staking

![Story Protocol](https://img.shields.io/badge/Story%20Protocol-Hackathon-blue) ![Solidity](https://img.shields.io/badge/Solidity-^0.8.20-red) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-green) ![React](https://img.shields.io/badge/React-19.1.0-blue)

> **Story Protocol Hackathon Project**  
> A decentralized trust staking system for Story Protocol IP assets with slash redistribution mechanism

## 🎯 Project Overview

### The Problem

In centralized systems, IP is protected by strict legal frameworks, so users do not bear much responsibility for verifying the legitimacy of copyrights. However, IP assets registered on **Story**, a decentralized platform, offer many advantages—but because anyone can freely register IP, users now have the responsibility to carefully evaluate whether an IP is legitimate.

For example, if someone creates derivative works based on an unfair IP, they may later suffer disadvantages as **Dispute Tags** begin to spread. Currently, there is a lack of effective tools for measuring the legitimacy of an IP asset. Likewise, IP owners lack a way to demonstrate the **credibility** of their IP.

### Our Solution

**Story IP Trust Staking** introduces a tool to assess Story IP credibility. Our core idea is: ***cost is trust*** — the same principle that underlies blockchain consensus, where blocks backed by higher costs are favored.

We propose the following features:
1. **Users stake cryptocurrency on IP assets** to show trust and support
2. **If an asset is found to be unfair, the stake is slashed** and distributed to trustworthy participants
3. **The credibility of an IP asset is measured by the amount staked** on it
4. **A UI allows users to check IP asset credibility** before making decisions

### Expected Outcomes

- **Creators stake** to prove their IP's trustworthiness and build reputation
- **Derivative creators** can safely choose credible IPs with confidence
- **Users earn rewards** by staking on legitimate IPs, contributing to a trust-based ecosystem
- **Self-regulating community** emerges through economic incentives

### 🔑 Key Features

- **🔒 IP Asset Staking**: Stake ETH on any Story Protocol IP asset to show trust
- **⚖️ Trust-Based Slashing**: Malicious or violating IP assets can be slashed by contract owner
- **🔄 Automatic Redistribution**: Slashed stakes are automatically redistributed to other IP asset stakers
- **📊 Transparent History**: Complete slash history with pagination support
- **🌐 Full-Stack DApp**: Smart contract, backend API, and frontend web interface

## 🏗️ Architecture

![Architecture Diagram](./assets/architecture-diagram.png)


## 🚀 Quick Start

### Prerequisites

- Node.js 18+
- Java 17+

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/story-protocol-buildaton
cd story-protocol-buildaton
```

### 2. Smart Contract Deployment

```bash
cd story-ip-trust-staking-smart-contract

# Deploy to testnet (update deployment script with your configuration)
# Contract Address: 0x6E0D112252335D08DBFCD1dA187Bcc43cdA62a9B (example)
```

### 3. Backend Setup

```bash
cd story-ip-trust-staking-server

# Run the application
./gradlew bootRun
```

The backend will be available at `http://localhost:8080`

### 4. Frontend Setup

```bash
cd story-ip-trust-staking-web/story-protocol-buildaton-web

# Install dependencies
npm install

# Start development server
npm start
```

The frontend will be available at `http://localhost:3000`

## 📋 Smart Contract Functions

### Core Staking Functions

```solidity
// Stake ETH on an IP asset
function stake(address ipAssetId) external payable

// Unstake ETH from an IP asset
function unstake(address ipAssetId, uint256 amount) external

// Slash an IP asset (owner only)
function slash(address ipAssetId) external onlyOwner
```

### View Functions

```solidity
// Get total staked amount for an IP asset
function getTotalStakedAmount(address ipAssetId) external view returns (uint256)

// Get user's staked amount for an IP asset
function getUserStakedAmount(address user, address ipAssetId) external view returns (uint256)

// Get all active IP assets
function getActiveIPAssets() external view returns (address[] memory)

// Get slash history with pagination
function getSlashHistoryPaginated(uint256 offset, uint256 limit) 
    external view returns (SlashRecord[] memory records, uint256 total)
```

## 🌐 API Endpoints

### Slash History API

```http
GET /api/v1/slash-history?offset=0&limit=10
```

**Response:**
```json
{
  "records": [
    {
      "slashedIPAsset": "0x123...",
      "redistributedToIPAsset": "0x456...",
      "amount": "1000000000000000000",
      "timestamp": "2024-01-01T00:00:00Z"
    }
  ],
  "total": 100,
  "offset": 0,
  "limit": 10,
  "hasNext": true
}
```

## 🎮 How It Works

### 1. Staking Process
1. User connects wallet to the DApp
2. User selects an IP asset from Story Protocol
3. User stakes ETH amount to show trust in the IP asset
4. Stakes are recorded on-chain with transparent tracking

### 2. Slashing Mechanism
1. Contract owner identifies malicious/violating IP asset
2. Owner calls `slash()` function for the problematic IP asset
3. All stakes for that IP asset are confiscated
4. Confiscated amounts are redistributed proportionally to stakers of other active IP assets

### 3. Redistribution Logic
- Random selection of target IP asset (excluding slashed one)
- Proportional distribution based on existing stake amounts
- Automatic balance updates for all affected stakers

## 🔧 Technical Implementation

### Smart Contract Features

- **OpenZeppelin Integration**: Uses `Ownable` for access control
- **Gas Optimization**: Batch functions for multiple IP assets
- **Security**: Reentrancy protection and input validation
- **Transparency**: Complete event logging for all operations

### Backend Architecture

- **Hexagonal Architecture**: Clean separation of concerns
- **Domain-Driven Design**: Business logic isolation
- **Spring Boot**: RESTful API with validation
- **PostgreSQL**: Data persistence and indexing

### Frontend Technology

- **React 19**: Modern UI with hooks
- **Ethers.js**: Web3 integration
- **Responsive Design**: Mobile-friendly interface

## 📊 Project Structure

```
story-protocol-buildaton/
├── story-ip-trust-staking-smart-contract/
│   └── story-ip-staking-module.sol          # Core smart contract
├── story-ip-trust-staking-server/           # Spring Boot backend
│   ├── src/main/kotlin/
│   ├── build.gradle.kts                     # Dependencies
│   └── slash-history-api.md                 # API documentation
└── story-ip-trust-staking-web/             # React frontend
    └── story-protocol-buildaton-web/
        ├── src/
        ├── public/
        └── package.json
```

## 🎯 Hackathon Highlights

### Innovation Points

1. **Trust-as-a-Service**: Novel approach to IP asset validation through economic incentives
2. **Self-Regulating Ecosystem**: Automatic redistribution creates natural selection pressure
3. **Transparent Governance**: Complete slash history with public audit trail
4. **Full-Stack Implementation**: End-to-end solution from smart contract to user interface

### Technical Achievements

- **Gas-Efficient Design**: Optimized smart contract with batch operations
- **Scalable Architecture**: Hexagonal architecture for maintainable code
- **User Experience**: Intuitive interface for complex blockchain operations
- **Security First**: Comprehensive input validation and access controls

## 🧪 Testing

### Smart Contract Testing
```bash
# Run contract tests
forge test
```

### Backend Testing
```bash
cd story-ip-trust-staking-server
./gradlew test
```

### Frontend Testing
```bash
cd story-ip-trust-staking-web/story-protocol-buildaton-web
npm test
```

## 🤝 Contributing

This project was built for the Story Protocol Hackathon. For development:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🎉 Acknowledgments

- **Story Protocol Team** for the innovative IP infrastructure
- **OpenZeppelin** for secure smart contract libraries
- **Ethereum Foundation** for the robust blockchain platform

## 📞 Contact

Built with ❤️ for Story Protocol Hackathon

- **Developer**: Chang Woo Jung
- **Project**: Story IP Trust Staking
- **Demo**: [Live Demo Link]
- **Presentation**: [Presentation Link]

---

*"Empowering IP creators through decentralized trust and economic incentives"* 🚀
