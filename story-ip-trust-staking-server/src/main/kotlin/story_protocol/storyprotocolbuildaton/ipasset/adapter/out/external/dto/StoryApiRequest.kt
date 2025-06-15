package story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class StoryApiRequest(
    @JsonProperty("options")
    val options: StoryApiQueryOptions
)

data class StoryApiQueryOptions(
    @JsonProperty("ipAssetIds")
    val ipAssetIds: List<String>? = null,
    
    @JsonProperty("orderBy")
    val orderBy: String? = null,
    
    @JsonProperty("orderDirection")
    val orderDirection: String? = null,
    
    @JsonProperty("pagination")
    val pagination: StoryApiPagination? = null,
    
    @JsonProperty("tokenContractIds")
    val tokenContractIds: List<String>? = null,
    
    @JsonProperty("tokenIds")
    val tokenIds: List<String>? = null,
    
    @JsonProperty("where")
    val where: StoryApiFilter? = null
)

data class StoryApiPagination(
    @JsonProperty("after")
    val after: String? = null,
    
    @JsonProperty("before")
    val before: String? = null,
    
    @JsonProperty("limit")
    val limit: Int? = null
)

data class StoryApiFilter(
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
