package story_protocol.storyprotocolbuildaton.ipasset.domain

import java.math.BigInteger

enum class TrustRank {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND;

    companion object {
        private val BRONZE_THRESHOLD = BigInteger("100000000000000")
        private val SILVER_THRESHOLD = BigInteger("200000000000000")
        private val GOLD_THRESHOLD = BigInteger("300000000000000")
        private val PLATINUM_THRESHOLD = BigInteger("400000000000000")
        private val DIAMOND_THRESHOLD = BigInteger("500000000000000")

        fun fromStakedAmount(stakedAmount: BigInteger): TrustRank {
            return when {
                stakedAmount >= DIAMOND_THRESHOLD -> DIAMOND
                stakedAmount >= PLATINUM_THRESHOLD -> PLATINUM
                stakedAmount >= GOLD_THRESHOLD -> GOLD
                stakedAmount >= SILVER_THRESHOLD -> SILVER
                stakedAmount >= BRONZE_THRESHOLD -> BRONZE
                else -> BRONZE
            }
        }
    }
}
