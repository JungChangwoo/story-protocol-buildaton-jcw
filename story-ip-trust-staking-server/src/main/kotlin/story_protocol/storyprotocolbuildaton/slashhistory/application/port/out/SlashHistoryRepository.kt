package story_protocol.storyprotocolbuildaton.slashhistory.application.port.out

import story_protocol.storyprotocolbuildaton.slashhistory.domain.PaginatedSlashHistory

interface SlashHistoryRepository {
    fun getSlashHistoryPaginated(offset: Long, limit: Long): PaginatedSlashHistory
}
