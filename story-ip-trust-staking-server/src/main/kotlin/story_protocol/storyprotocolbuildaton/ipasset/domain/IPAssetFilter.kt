package story_protocol.storyprotocolbuildaton.ipasset.domain

data class IPAssetFilter(
    val blockNumber: String? = null,
    val blockNumberGte: String? = null,
    val blockNumberLte: String? = null,
    val id: String? = null,
    val ipId: String? = null,
    val tokenContract: String? = null,
    val tokenId: String? = null
)
