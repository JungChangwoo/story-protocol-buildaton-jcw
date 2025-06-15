package story_protocol.storyprotocolbuildaton.ipasset.domain

import java.math.BigInteger

data class SlashRecord(
    val slashedIPAsset: String,
    val redistributedToIPAsset: String,
    val amount: BigInteger,
    val timestamp: Long
)