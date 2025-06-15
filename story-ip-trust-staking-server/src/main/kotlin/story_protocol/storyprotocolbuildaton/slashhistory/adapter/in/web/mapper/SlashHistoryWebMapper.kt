package story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web.mapper

import story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web.dto.PaginatedSlashHistoryDto
import story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web.dto.SlashRecordDto
import story_protocol.storyprotocolbuildaton.slashhistory.domain.PaginatedSlashHistory
import story_protocol.storyprotocolbuildaton.slashhistory.domain.SlashRecord

object SlashHistoryWebMapper {
    
    fun toDto(domain: PaginatedSlashHistory): PaginatedSlashHistoryDto {
        return PaginatedSlashHistoryDto(
            records = domain.records.map { toDto(it) },
            total = domain.total,
            offset = domain.offset,
            limit = domain.limit,
            hasNext = domain.offset + domain.limit < domain.total
        )
    }
    
    private fun toDto(domain: SlashRecord): SlashRecordDto {
        return SlashRecordDto(
            slashedIPAsset = domain.slashedIPAsset,
            redistributedToIPAsset = domain.redistributedToIPAsset,
            amount = domain.amount,
            timestamp = domain.timestamp
        )
    }
}
