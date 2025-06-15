package story_protocol.storyprotocolbuildaton.slashhistory.domain

import java.time.Instant

data class SlashRecord(
    val slashedIPAsset: String,
    val redistributedToIPAsset: String,
    val amount: String,
    val timestamp: Instant
)
