package story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web.dto.PaginatedSlashHistoryDto
import story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web.mapper.SlashHistoryWebMapper
import story_protocol.storyprotocolbuildaton.slashhistory.application.port.`in`.GetSlashHistoryUseCase

@RestController
@RequestMapping("/api/v1/slash-history")
class SlashHistoryController(
    private val getSlashHistoryUseCase: GetSlashHistoryUseCase
) {

    @GetMapping
    fun getSlashHistory(
        @RequestParam(defaultValue = "0") offset: Long,
        @RequestParam(defaultValue = "10") limit: Long
    ): PaginatedSlashHistoryDto {
        require(offset >= 0) { "Offset must be non-negative" }
        require(limit > 0 && limit <= 100) { "Limit must be between 1 and 100" }
        
        val result = getSlashHistoryUseCase.getSlashHistory(offset, limit)
        return SlashHistoryWebMapper.toDto(result)
    }
}
