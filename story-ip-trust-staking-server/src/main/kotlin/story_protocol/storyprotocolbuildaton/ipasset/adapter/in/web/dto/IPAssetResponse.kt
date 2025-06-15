package story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class IPAssetsResponse(
    @JsonProperty("data")
    val data: List<IPAssetDto>,
    
    @JsonProperty("hasNextPage")
    val hasNextPage: Boolean,
    
    @JsonProperty("hasPreviousPage")
    val hasPreviousPage: Boolean,
    
    @JsonProperty("next")
    val next: String?,
    
    @JsonProperty("prev")
    val prev: String?
)

data class IPAssetDto(
    @JsonProperty("ancestorCount")
    val ancestorCount: Int,
    
    @JsonProperty("blockNumber")
    val blockNumber: String,
    
    @JsonProperty("blockTimestamp")
    val blockTimestamp: String,
    
    @JsonProperty("childrenCount")
    val childrenCount: Int,
    
    @JsonProperty("descendantCount")
    val descendantCount: Int,
    
    @JsonProperty("id")
    val id: String,
    
    @JsonProperty("ipId")
    val ipId: String,
    
    @JsonProperty("isGroup")
    val isGroup: Boolean,
    
    @JsonProperty("latestArbitrationPolicy")
    val latestArbitrationPolicy: String?,
    
    @JsonProperty("nftMetadata")
    val nftMetadata: NftMetadataDto?,
    
    @JsonProperty("parentCount")
    val parentCount: Int,
    
    @JsonProperty("rootCount")
    val rootCount: Int,
    
    @JsonProperty("rootIpIds")
    val rootIpIds: List<String>,
    
    @JsonProperty("transactionHash")
    val transactionHash: String,
    
    @JsonProperty("trustRank")
    val trustRank: String,
    
    @JsonProperty("stakingInfo")
    val stakingInfo: StakingInfoDto
)

data class NftMetadataDto(
    @JsonProperty("chainId")
    val chainId: String,
    
    @JsonProperty("imageUrl")
    val imageUrl: String?,
    
    @JsonProperty("name")
    val name: String?,
    
    @JsonProperty("tokenContract")
    val tokenContract: String,
    
    @JsonProperty("tokenId")
    val tokenId: String,
    
    @JsonProperty("tokenUri")
    val tokenUri: String?
)

data class StakingInfoDto(
    @JsonProperty("totalStakedAmount")
    val totalStakedAmount: String,
    
    @JsonProperty("userStakedAmount")
    val userStakedAmount: String?,
    
    @JsonProperty("slashHistory")
    val slashHistory: List<SlashRecordDto>
)

data class SlashRecordDto(
    @JsonProperty("slashedIPAsset")
    val slashedIPAsset: String,
    
    @JsonProperty("redistributedToIPAsset")
    val redistributedToIPAsset: String,
    
    @JsonProperty("amount")
    val amount: String,
    
    @JsonProperty("timestamp")
    val timestamp: Long
)
