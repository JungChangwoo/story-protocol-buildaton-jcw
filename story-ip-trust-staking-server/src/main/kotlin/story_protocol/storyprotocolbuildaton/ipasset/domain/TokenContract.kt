package story_protocol.storyprotocolbuildaton.ipasset.domain

@JvmInline
value class TokenContract(val value: String) {
    init {
        require(value.isNotBlank()) { "Token contract cannot be blank" }
    }
}
