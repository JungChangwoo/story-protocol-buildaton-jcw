package story_protocol.storyprotocolbuildaton.slashhistory.domain

data class PaginatedSlashHistory(
    val records: List<SlashRecord>,
    val total: Long,
    val offset: Long,
    val limit: Long
)
