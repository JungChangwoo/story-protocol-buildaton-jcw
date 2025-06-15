package story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class StoryApiResponse(
    @JsonProperty("data")
    val data: List<StoryApiIPAsset>,
    
    @JsonProperty("hasNextPage")
    val hasNextPage: Boolean,
    
    @JsonProperty("hasPreviousPage")
    val hasPreviousPage: Boolean,
    
    @JsonProperty("next")
    val next: String?,
    
    @JsonProperty("prev")
    val prev: String?
)

data class StoryApiIPAsset(
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
    val nftMetadata: StoryApiNftMetadata?,
    
    @JsonProperty("parentCount")
    val parentCount: Int,
    
    @JsonProperty("rootCount")
    val rootCount: Int,
    
    @JsonProperty("rootIpIds")
    val rootIpIds: List<String>,
    
    @JsonProperty("transactionHash")
    val transactionHash: String
)

data class StoryApiNftMetadata(
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
