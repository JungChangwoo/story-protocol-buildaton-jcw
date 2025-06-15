# Backend Server - Story IP Trust Staking API

## Overview

Spring Boot backend service providing REST APIs for the Story IP Trust Staking system. Built with Hexagonal Architecture and Domain-Driven Design principles.

## Tech Stack

- **Spring Boot 3.5.0**
- **Kotlin**
- **QueryDSL**
- **OpenFeign** for external API calls

## Quick Start

### Prerequisites
- Java 17+
- Smart contract deployed and configured

### Environment Setup

1. Configure application in `application.yml`:
```yaml
blockchain:
  rpc:
    url: ${BLOCKCHAIN_RPC_URL:https://sepolia.infura.io/v3/...}

staking:
  contract:
    address: ${STAKING_CONTRACT_ADDRESS:0x6E0D112252335D08DBFCD1dA187Bcc43cdA62a9B}
```

2. Run the application:
```bash
./gradlew bootRun
```

The server will start on `http://localhost:8080`

## API Endpoints

### Slash History API

#### GET `/api/v1/slash-history`

Get paginated slash history records from the smart contract.

**Parameters:**
- `offset` (optional): Starting position (default: 0)
- `limit` (optional): Number of records (default: 10, max: 100)

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

**Example Usage:**
```bash
# Basic query
curl -X GET "http://localhost:8080/api/v1/slash-history"

# With pagination
curl -X GET "http://localhost:8080/api/v1/slash-history?offset=10&limit=20"
```

## Architecture

### Package Structure
```
src/main/kotlin/slashhistory/
├── domain/                     # Core business entities
│   ├── SlashRecord.kt
│   └── PaginatedSlashHistory.kt
├── application/                # Business logic & ports
│   ├── port/
│   │   ├── in/GetSlashHistoryUseCase.kt
│   │   └── out/SlashHistoryRepository.kt
│   └── service/SlashHistoryService.kt
└── adapter/                    # External integrations
    ├── in/web/SlashHistoryController.kt
    └── out/blockchain/SlashHistoryBlockchainAdapter.kt
```

### Design Principles

- **Hexagonal Architecture**: Clean separation between business logic and external concerns
- **Domain-Driven Design**: Business rules encapsulated in domain layer
- **Dependency Inversion**: Dependencies point inward toward business logic

## Testing

```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport
```

## Docker Support

```bash
# Build and run application
./gradlew build
docker build -t story-staking-server .
docker run -p 8080:8080 story-staking-server
```

## Configuration

### Blockchain Integration

The application integrates with the deployed smart contract to fetch slash history data. Make sure to configure the correct contract address and RPC URL.

---

📄 **License**: MIT  
🔗 **Main Project**: [../README.md](../README.md)  
📖 **API Documentation**: [slash-history-api.md](slash-history-api.md)
