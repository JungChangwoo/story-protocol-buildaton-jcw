package story_protocol.storyprotocolbuildaton.slashhistory.adapter.`in`.web.dto

import java.time.Instant

data class SlashRecordDto(
    val slashedIPAsset: String,
    val redistributedToIPAsset: String,
    val amount: String,
    val timestamp: Instant
)
