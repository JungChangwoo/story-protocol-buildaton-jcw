package story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web.dto

data class PaginatedSlashHistoryDto(
    val records: List<SlashRecordDto>,
    val total: Long,
    val offset: Long,
    val limit: Long,
    val hasNext: Boolean
)
