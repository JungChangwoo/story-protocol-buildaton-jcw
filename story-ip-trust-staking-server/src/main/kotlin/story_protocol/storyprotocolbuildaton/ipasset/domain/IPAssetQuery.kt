package story_protocol.storyprotocolbuildaton.ipasset.domain

data class IPAssetQuery(
    val ipAssetIds: List<String>? = null,
    val orderBy: OrderBy? = OrderBy.ID,
    val orderDirection: OrderDirection? = OrderDirection.ASC,
    val pagination: Pagination? = null,
    val tokenContractIds: List<String>? = null,
    val tokenIds: List<String>? = null,
    val where: IPAssetFilter? = null,
    val userAddress: String? = null
)
