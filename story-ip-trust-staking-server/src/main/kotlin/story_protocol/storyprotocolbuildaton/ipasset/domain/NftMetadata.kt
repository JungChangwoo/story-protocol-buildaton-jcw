package story_protocol.storyprotocolbuildaton.ipasset.domain

data class NftMetadata(
    val chainId: String,
    val imageUrl: String?,
    val name: String?,
    val tokenContract: String,
    val tokenId: String,
    val tokenUri: String?
)
