package story_protocol.storyprotocolbuildaton.slashhistory.application.port.`in`

import story_protocol.storyprotocolbuildaton.slashhistory.domain.PaginatedSlashHistory

interface GetSlashHistoryUseCase {
    fun getSlashHistory(offset: Long, limit: Long): PaginatedSlashHistory
}
