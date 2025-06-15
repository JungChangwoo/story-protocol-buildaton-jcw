// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "@openzeppelin/contracts/access/Ownable.sol";

contract IPStakingModule is Ownable {
    mapping(address => mapping(address => uint256)) public userStakes;
    mapping(address => uint256) public totalStakes;
    mapping(address => address[]) public stakersPerIP;
    mapping(address => mapping(address => bool)) public hasStaked;
    
    // Track all IP assets that have stakes
    address[] public activeIPAssets;
    mapping(address => bool) public isActiveIPAsset;

    // Slash history tracking
    struct SlashRecord {
        address slashedIPAsset;
        address redistributedToIPAsset;
        uint256 amount;
        uint256 timestamp;
    }
    
    SlashRecord[] public slashHistory;
    mapping(address => uint256[]) public ipAssetSlashHistory; // IP asset => slash record indices

    event Staked(address indexed user, address indexed ipAssetId, uint256 amount);
    event Unstaked(address indexed user, address indexed ipAssetId, uint256 amount);
    event Slashed(address indexed ipAssetId, uint256 totalSlashedAmount);
    event SlashRedistributed(address indexed fromIPAsset, address indexed toIPAsset, uint256 amount);

    constructor() Ownable(msg.sender) {}

    function stake(address ipAssetId) external payable {
        require(msg.value > 0, "Amount must be greater than zero");
        require(ipAssetId != address(0), "IP Asset ID cannot be zero address");

        // Add to active IP assets list if not already present
        if (!isActiveIPAsset[ipAssetId]) {
            activeIPAssets.push(ipAssetId);
            isActiveIPAsset[ipAssetId] = true;
        }

        // Add user to stakers list if first time staking
        if (!hasStaked[ipAssetId][msg.sender]) {
            stakersPerIP[ipAssetId].push(msg.sender);
            hasStaked[ipAssetId][msg.sender] = true;
        }

        userStakes[msg.sender][ipAssetId] += msg.value;
        totalStakes[ipAssetId] += msg.value;

        emit Staked(msg.sender, ipAssetId, msg.value);
    }

    function unstake(address ipAssetId, uint256 amount) external {
        require(amount > 0, "Amount must be greater than zero");
        require(userStakes[msg.sender][ipAssetId] >= amount, "Insufficient staked amount");
        require(ipAssetId != address(0), "IP Asset ID cannot be zero address");

        userStakes[msg.sender][ipAssetId] -= amount;
        totalStakes[ipAssetId] -= amount;

        (bool success, ) = payable(msg.sender).call{value: amount}("");
        require(success, "ETH transfer failed during unstaking");

        emit Unstaked(msg.sender, ipAssetId, amount);
    }

    function slash(address ipAssetId) external onlyOwner {
        require(ipAssetId != address(0), "IP Asset ID cannot be zero address");
        require(totalStakes[ipAssetId] > 0, "No stake exists for this IP asset");

        uint256 totalSlashedAmount = totalStakes[ipAssetId];
        address[] memory stakers = stakersPerIP[ipAssetId];

        // Clear all stakes for the slashed IP asset
        for (uint256 i = 0; i < stakers.length; i++) {
            address staker = stakers[i];
            userStakes[staker][ipAssetId] = 0;
        }

        totalStakes[ipAssetId] = 0;

        // Find a random IP asset to redistribute to (excluding the slashed one)
        address targetIPAsset = _getRandomIPAssetExcluding(ipAssetId);
        
        // Record slash history
        SlashRecord memory newRecord = SlashRecord({
            slashedIPAsset: ipAssetId,
            redistributedToIPAsset: targetIPAsset,
            amount: totalSlashedAmount,
            timestamp: block.timestamp
        });
        
        slashHistory.push(newRecord);
        uint256 recordIndex = slashHistory.length - 1;
        
        // Add to IP asset specific history
        ipAssetSlashHistory[ipAssetId].push(recordIndex);
        if (targetIPAsset != address(0)) {
            ipAssetSlashHistory[targetIPAsset].push(recordIndex);
        }
        
        if (targetIPAsset != address(0)) {
            _redistributeAmount(targetIPAsset, totalSlashedAmount);
            emit SlashRedistributed(ipAssetId, targetIPAsset, totalSlashedAmount);
        }

        emit Slashed(ipAssetId, totalSlashedAmount);
    }

    // Get random IP asset excluding the specified one
    function _getRandomIPAssetExcluding(address excludeIPAsset) internal view returns (address) {
        address[] memory validIPAssets = new address[](activeIPAssets.length);
        uint256 validCount = 0;

        // Collect valid IP assets (excluding the slashed one and those with no stakes)
        for (uint256 i = 0; i < activeIPAssets.length; i++) {
            address ipAsset = activeIPAssets[i];
            if (ipAsset != excludeIPAsset && totalStakes[ipAsset] > 0) {
                validIPAssets[validCount] = ipAsset;
                validCount++;
            }
        }

        if (validCount == 0) {
            return address(0); // No valid IP assets found
        }

        // Generate pseudo-random index
        uint256 randomIndex = uint256(keccak256(abi.encodePacked(
            block.timestamp,
            block.prevrandao,
            excludeIPAsset,
            validCount
        ))) % validCount;

        return validIPAssets[randomIndex];
    }

    // Redistribute amount proportionally to existing stakers
    function _redistributeAmount(address targetIPAsset, uint256 amount) internal {
        address[] memory stakers = stakersPerIP[targetIPAsset];
        uint256 targetTotalStakes = totalStakes[targetIPAsset];

        if (targetTotalStakes == 0) {
            return; // No stakers to redistribute to
        }

        // Redistribute proportionally to each staker
        for (uint256 i = 0; i < stakers.length; i++) {
            address staker = stakers[i];
            uint256 stakerAmount = userStakes[staker][targetIPAsset];
            
            if (stakerAmount > 0) {
                uint256 proportionalAmount = (amount * stakerAmount) / targetTotalStakes;
                userStakes[staker][targetIPAsset] += proportionalAmount;
            }
        }

        // Update total stakes for target IP asset
        totalStakes[targetIPAsset] += amount;
    }

    // Original single IP asset function
    function getTotalStakedAmount(address ipAssetId) external view returns (uint256) {
        return totalStakes[ipAssetId];
    }

    // New batch function for multiple IP assets
    function getTotalStakedAmounts(address[] calldata ipAssetIds) 
        external 
        view 
        returns (uint256[] memory amounts) 
    {
        require(ipAssetIds.length > 0, "IP Asset IDs array cannot be empty");
        require(ipAssetIds.length <= 100, "Too many IP Asset IDs requested"); // Gas limit protection
        
        amounts = new uint256[](ipAssetIds.length);
        
        for (uint256 i = 0; i < ipAssetIds.length; i++) {
            amounts[i] = totalStakes[ipAssetIds[i]];
        }
        
        return amounts;
    }

    function getUserStakedAmount(address user, address ipAssetId) external view returns (uint256) {
        return userStakes[user][ipAssetId];
    }

    // New batch function for user stakes across multiple IP assets
    function getUserStakedAmounts(address user, address[] calldata ipAssetIds) 
        external 
        view 
        returns (uint256[] memory amounts) 
    {
        require(ipAssetIds.length > 0, "IP Asset IDs array cannot be empty");
        require(ipAssetIds.length <= 100, "Too many IP Asset IDs requested"); // Gas limit protection
        
        amounts = new uint256[](ipAssetIds.length);
        
        for (uint256 i = 0; i < ipAssetIds.length; i++) {
            amounts[i] = userStakes[user][ipAssetIds[i]];
        }
        
        return amounts;
    }

    // View function to get all active IP assets
    function getActiveIPAssets() external view returns (address[] memory) {
        return activeIPAssets;
    }

    // Get total number of slash records
    function getSlashHistoryLength() external view returns (uint256) {
        return slashHistory.length;
    }

    // Get slash records for multiple IP assets
    function getIPAssetsSlashHistory(address[] calldata ipAssetIds) 
        external 
        view 
        returns (SlashRecord[][] memory) 
    {
        require(ipAssetIds.length > 0, "IP Asset IDs array cannot be empty");
        require(ipAssetIds.length <= 50, "Too many IP Asset IDs requested"); // Gas limit protection
        
        SlashRecord[][] memory allRecords = new SlashRecord[][](ipAssetIds.length);
        
        for (uint256 i = 0; i < ipAssetIds.length; i++) {
            uint256[] memory indices = ipAssetSlashHistory[ipAssetIds[i]];
            SlashRecord[] memory records = new SlashRecord[](indices.length);
            
            for (uint256 j = 0; j < indices.length; j++) {
                records[j] = slashHistory[indices[j]];
            }
            
            allRecords[i] = records;
        }
        
        return allRecords;
    }

    // Get all slash records with pagination
    function getSlashHistoryPaginated(uint256 offset, uint256 limit) 
        external 
        view 
        returns (SlashRecord[] memory records, uint256 total) 
    {
        total = slashHistory.length;
        
        if (offset >= total) {
            return (new SlashRecord[](0), total);
        }
        
        uint256 end = offset + limit;
        if (end > total) {
            end = total;
        }
        
        uint256 length = end - offset;
        records = new SlashRecord[](length);
        
        for (uint256 i = 0; i < length; i++) {
            records[i] = slashHistory[offset + i];
        }
        
        return (records, total);
    }

    receive() external payable {}
}