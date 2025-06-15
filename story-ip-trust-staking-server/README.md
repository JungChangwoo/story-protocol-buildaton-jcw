# Backend Server - Story IP Trust Staking API

## Overview

Spring Boot backend service providing REST APIs for the Story IP Trust Staking system. Built with Hexagonal Architecture and Domain-Driven Design principles.

## Tech Stack

- **Spring Boot 3.5.0**
- **Kotlin**
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

**Example Usage:**
```bash
# Basic query
curl -X GET "http://localhost:8080/api/v1/slash-history"

# With pagination
curl -X GET "http://localhost:8080/api/v1/slash-history?offset=10&limit=20"
```

### Assets API

#### GET `/api/v3/assets`

Get IP assets with staking information and credibility scores.

**Example Usage:**
```bash
# Get all assets
curl -X GET "http://localhost:8080/api/v3/assets"
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

