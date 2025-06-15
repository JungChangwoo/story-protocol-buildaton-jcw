# Slash History API

## Overview
슬래시 히스토리 조회 API는 IP Asset의 슬래시 이벤트 기록을 페이지네이션으로 조회할 수 있는 기능을 제공합니다.

## Architecture
헥사고날 아키텍처(Hexagonal Architecture)와 도메인 주도 설계(Domain Driven Design) 원칙을 따라 구현되었습니다.

### Package Structure
```
slashhistory/
├── domain/                     # 핵심 비즈니스 규칙과 엔티티
│   ├── SlashRecord.kt
│   └── PaginatedSlashHistory.kt
├── application/                # 비즈니스 로직과 포트 정의
│   ├── port/
│   │   ├── in/
│   │   │   └── GetSlashHistoryUseCase.kt
│   │   └── out/
│   │       └── SlashHistoryRepository.kt
│   └── service/
│       └── SlashHistoryService.kt
└── adapter/                    # 외부 시스템과의 통신
    ├── in/
    │   └── web/
    │       ├── SlashHistoryController.kt
    │       ├── dto/
    │       ├── mapper/
    │       └── exception/
    └── out/
        └── blockchain/
            └── SlashHistoryBlockchainAdapter.kt
```

## API Endpoints

### GET /api/v1/slash-history

스마트 컨트랙트의 `getSlashHistoryPaginated` 함수를 호출하여 슬래시 히스토리를 조회합니다.

#### Parameters
- `offset` (optional): 조회 시작 위치 (기본값: 0)
- `limit` (optional): 조회할 레코드 수 (기본값: 10, 최대: 100)

#### Response
```json
{
  "records": [
    {
      "slashedIPAsset": "0x...",
      "redistributedToIPAsset": "0x...",
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

#### Example Usage
```bash
# 기본 조회
curl -X GET "http://localhost:8080/api/v1/slash-history"

# 페이지네이션을 사용한 조회
curl -X GET "http://localhost:8080/api/v1/slash-history?offset=10&limit=20"

# 특정 개수만 조회
curl -X GET "http://localhost:8080/api/v1/slash-history?limit=5"
```

## Error Responses

### 400 Bad Request
```json
{
  "code": "INVALID_PARAMETER",
  "message": "Offset must be non-negative"
}
```

### 500 Internal Server Error
```json
{
  "code": "INTERNAL_SERVER_ERROR",
  "message": "An internal server error occurred"
}
```

## Configuration

### application.yml
```yaml
blockchain:
  rpc:
    url: ${BLOCKCHAIN_RPC_URL:https://sepolia.infura.io/v3/...}

staking:
  contract:
    address: ${STAKING_CONTRACT_ADDRESS:0x6E0D112252335D08DBFCD1dA187Bcc43cdA62a9B}
```

## Smart Contract Integration

이 API는 스마트 컨트랙트의 `getSlashHistoryPaginated(uint256 offset, uint256 limit)` 함수를 호출합니다.

### Contract Function Signature
- Function Selector: `0x8ffaa2fc`
- Returns: `(SlashRecord[] records, uint256 total)`

### SlashRecord Structure
```solidity
struct SlashRecord {
    address slashedIPAsset;
    address redistributedToIPAsset;
    uint256 amount;
    uint256 timestamp;
}
```
