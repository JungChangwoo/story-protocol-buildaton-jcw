package story_protocol.storyprotocolbuildaton.ipasset.domain

import java.math.BigInteger

data class StakingInfo(
    val totalStakedAmount: BigInteger,
    val userStakedAmount: BigInteger?,
    val slashHistory: List<SlashRecord>
) {
    companion object {
        fun create(totalStakedAmount: BigInteger, userStakedAmount: BigInteger?, slashHistory: List<SlashRecord> = emptyList()): StakingInfo {
            return StakingInfo(
                totalStakedAmount = totalStakedAmount,
                userStakedAmount = userStakedAmount,
                slashHistory = slashHistory
            )
        }
        
        fun empty(): StakingInfo {
            return StakingInfo(
                totalStakedAmount = BigInteger.ZERO,
                userStakedAmount = null,
                slashHistory = emptyList()
            )
        }
    }
}
