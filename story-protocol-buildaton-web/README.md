# Story Protocol Buildathon Web Application

이 웹 애플리케이션은 Story Protocol의 IP Asset을 시각화하여 보여주는 React 애플리케이션입니다.

## 기능

- **IP Asset 목록 표시**: Story Protocol API에서 IP Asset 데이터를 가져와 카드 형태로 표시
- **주요 정보 강조**: 각 Asset의 이름(name), 이미지(imageUrl), 신뢰도(trustRank)를 중점적으로 표시
- **TrustRank 시각화**: BRONZE, SILVER, GOLD, PLATINUM, DIAMOND 등급에 따른 이미지와 색상 구분
- **TrustRank 배지**: 각 카드에 등급을 나타내는 배지와 이모지 표시
- **인터랙티브 필터링**: TrustRank 통계를 클릭하여 해당 등급 필터링 가능
- **검색 기능**: 이름이나 IP ID로 자산을 실시간 검색
- **필터링 및 정렬**: 블록 번호, 신뢰도, 자식 개수 기준으로 정렬 가능
- **검색어 하이라이트**: 검색 결과에서 일치하는 텍스트 강조 표시
- **TrustRank 통계**: 전체 자산의 등급별 분포 시각화
- **반응형 디자인**: 다양한 화면 크기에 대응하는 그리드 레이아웃
- **로딩 및 에러 처리**: 사용자 친화적인 로딩 스피너와 에러 메시지

## 실행 방법

### 1. 백엔드 서버 실행

먼저 백엔드 서버가 실행되어야 합니다:

```bash
cd /$server_path
./gradlew bootRun
```

백엔드 서버는 `http://localhost:8080`에서 실행됩니다.

### 2. 프론트엔드 웹 애플리케이션 실행

```bash
cd /$web_path
npm install
npm start
```

웹 애플리케이션은 `http://localhost:3000`에서 실행됩니다.

## 주요 컴포넌트

### App.js
- 메인 애플리케이션 컴포넌트
- API 호출 및 상태 관리
- 검색, 필터링, 정렬 로직
- 로딩, 에러, 데이터 표시 로직

### SearchAndFilter.js
- 검색 입력 필드와 필터 옵션 제공
- 실시간 검색 및 정렬 기준 변경

### IPAssetList.js
- IP Asset 목록을 그리드 형태로 표시
- 검색 결과 요약 정보 표시
- 빈 상태 처리

### IPAssetCard.js
- 개별 IP Asset을 카드 형태로 표시
- 이미지, 이름, Trust Rank 강조
- 검색어 하이라이트 기능
- Trust Rank에 따른 색상 구분
- 메타데이터 정보 표시

### LoadingSpinner.js
- 로딩 상태 표시 컴포넌트

### ErrorMessage.js
- 에러 메시지 및 재시도 버튼 제공

## 스타일링

- **그라디언트 헤더**: 모던한 느낌의 배경
- **카드 기반 레이아웃**: 각 Asset을 독립적인 카드로 표시
- **호버 효과**: 카드에 마우스 오버 시 그림자와 변형 효과
- **반응형 그리드**: 화면 크기에 따라 컬럼 수 조정
- **Trust Rank 강조**: 그라디언트 배경으로 신뢰도 점수 강조

## 개발 환경

- **React**: 19.1.0
- **Node.js**: 최신 LTS 버전 권장
- **백엔드**: Spring Boot (Kotlin)
- **API**: Story Protocol API

## 문제 해결

### CORS 이슈
프록시 설정(`"proxy": "http://localhost:8080"`)이 package.json에 추가되어 있어 개발 환경에서 CORS 이슈가 해결됩니다.

### 백엔드 연결 실패
1. 백엔드 서버가 8080 포트에서 실행 중인지 확인
2. 백엔드의 CORS 설정 확인
3. 네트워크 방화벽 설정 확인

### 이미지 로딩 실패
이미지 URL이 유효하지 않거나 HTTPS/HTTP 문제가 있을 경우 placeholder 이미지가 표시됩니다.
