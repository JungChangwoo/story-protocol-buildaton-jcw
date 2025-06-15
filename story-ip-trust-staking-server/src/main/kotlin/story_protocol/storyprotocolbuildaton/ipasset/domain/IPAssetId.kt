package story_protocol.storyprotocolbuildaton.ipasset.domain

@JvmInline
value class IPAssetId(val value: String) {
    init {
        require(value.isNotBlank()) { "IPAsset ID cannot be blank" }
    }
}
