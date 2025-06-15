# Story Protocol Buildathon Web Application

This web application is a React application that visualizes and displays Story Protocol IP Assets.

## Features

- **IP Asset List Display**: Fetches IP Asset data from Story Protocol API and displays it in card format
- **Key Information Highlight**: Emphasizes each asset's name, imageUrl, and trustRank
- **TrustRank Visualization**: Image and color differentiation based on BRONZE, SILVER, GOLD, PLATINUM, DIAMOND grades
- **TrustRank Badges**: Displays badges and emojis representing grades on each card
- **Interactive Filtering**: Click on TrustRank statistics to filter by corresponding grade
- **Search Functionality**: Real-time search by name or IP ID
- **Filtering and Sorting**: Sort by block number, trust score, or child count
- **Search Highlight**: Emphasizes matching text in search results
- **TrustRank Statistics**: Visualizes grade distribution of all assets
- **Responsive Design**: Grid layout that adapts to various screen sizes
- **Loading and Error Handling**: User-friendly loading spinner and error messages

## How to Run

### 1. Start Backend Server

First, the backend server must be running:

```bash
cd /$server_path
./gradlew bootRun
```

The backend server runs on `http://localhost:8080`.

### 2. Run Frontend Web Application

```bash
cd /$web_path
npm install
npm start
```

The web application runs on `http://localhost:3000`.

## Main Components

### App.js
- Main application component
- API calls and state management
- Search, filtering, and sorting logic
- Loading, error, and data display logic

### SearchAndFilter.js
- Provides search input field and filter options
- Real-time search and sorting criteria changes

### IPAssetList.js
- Displays IP Asset list in grid format
- Shows search result summary information
- Handles empty state

### IPAssetCard.js
- Displays individual IP Assets in card format
- Emphasizes image, name, and Trust Rank
- Search term highlighting functionality
- Color differentiation based on Trust Rank
- Displays metadata information

### LoadingSpinner.js
- Loading state display component

### ErrorMessage.js
- Provides error messages and retry button

## Styling

- **Gradient Header**: Modern background feel
- **Card-based Layout**: Displays each asset as independent cards
- **Hover Effects**: Shadow and transform effects on mouse over cards
- **Responsive Grid**: Adjusts column count based on screen size
- **Trust Rank Emphasis**: Emphasizes trust scores with gradient backgrounds

## Development Environment

- **React**: 19.1.0
- **Node.js**: Latest LTS version recommended
