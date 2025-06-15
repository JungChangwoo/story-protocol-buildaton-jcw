package story_protocol.storyprotocolbuildaton.ipasset.domain

data class Pagination(
    val after: String? = null,
    val before: String? = null,
    val limit: Int? = null
) {
    init {
        limit?.let {
            require(it > 0) { "Limit must be positive" }
            require(it <= 100) { "Limit cannot exceed 100" }
        }
    }
}
