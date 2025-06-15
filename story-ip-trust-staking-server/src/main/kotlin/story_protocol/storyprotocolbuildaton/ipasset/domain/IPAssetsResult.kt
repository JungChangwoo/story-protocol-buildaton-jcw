package story_protocol.storyprotocolbuildaton.ipasset.domain

data class IPAssetsResult(
    val data: List<IPAsset>,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val next: String?,
    val prev: String?
)
