package story_protocol.storyprotocolbuildaton.ipasset.domain

@JvmInline
value class TokenId(val value: String) {
    init {
        require(value.isNotBlank()) { "Token ID cannot be blank" }
    }
}
