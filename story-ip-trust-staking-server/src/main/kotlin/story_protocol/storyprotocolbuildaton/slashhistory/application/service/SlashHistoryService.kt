package story_protocol.storyprotocolbuildaton.slashhistory.application.service

import org.springframework.stereotype.Service
import story_protocol.storyprotocolbuildaton.slashhistory.application.port.`in`.GetSlashHistoryUseCase
import story_protocol.storyprotocolbuildaton.slashhistory.application.port.out.SlashHistoryRepository
import story_protocol.storyprotocolbuildaton.slashhistory.domain.PaginatedSlashHistory

@Service
class SlashHistoryService(
    private val slashHistoryRepository: SlashHistoryRepository
) : GetSlashHistoryUseCase {

    override fun getSlashHistory(offset: Long, limit: Long): PaginatedSlashHistory {
        require(offset >= 0) { "Offset must be non-negative" }
        require(limit > 0) { "Limit must be positive" }
        require(limit <= 100) { "Limit must not exceed 100" }
        
        return slashHistoryRepository.getSlashHistoryPaginated(offset, limit)
    }
}
