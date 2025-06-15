package story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class IPAssetQueryRequest(
    val options: IPAssetQueryOptions,
    val userAddress: String? = null
)

data class IPAssetQueryOptions(
    @JsonProperty("ipAssetIds")
    val ipAssetIds: List<String>? = null,
    
    @JsonProperty("orderBy")
    val orderBy: String? = "blocknumber",
    
    @JsonProperty("orderDirection")
    val orderDirection: String? = "desc",
    
    @JsonProperty("pagination")
    val pagination: PaginationRequest? = null,
    
    @JsonProperty("tokenContractIds")
    val tokenContractIds: List<String>? = null,
    
    @JsonProperty("tokenIds")
    val tokenIds: List<String>? = null,
    
    @JsonProperty("where")
    val where: IPAssetFilterRequest? = null
)

data class PaginationRequest(
    @JsonProperty("after")
    val after: String? = null,
    
    @JsonProperty("before")
    val before: String? = null,
    
    @JsonProperty("limit")
    val limit: Int? = null
)

data class IPAssetFilterRequest(
    @JsonProperty("blockNumber")
    val blockNumber: String? = null,
    
    @JsonProperty("blockNumberGte")
    val blockNumberGte: String? = null,
    
    @JsonProperty("blockNumberLte")
    val blockNumberLte: String? = null,
    
    @JsonProperty("id")
    val id: String? = null,
    
    @JsonProperty("ipId")
    val ipId: String? = null,
    
    @JsonProperty("tokenContract")
    val tokenContract: String? = null,
    
    @JsonProperty("tokenId")
    val tokenId: String? = null
)
