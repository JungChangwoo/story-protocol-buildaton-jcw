package story_protocol.storyprotocolbuildaton.ipasset.application.port.out

import story_protocol.storyprotocolbuildaton.ipasset.domain.SlashRecord
import java.math.BigInteger

interface StakingContractRepository {
    fun getTotalStakedAmounts(ipAssetIds: List<String>): Map<String, BigInteger>
    fun getUserStakedAmounts(userAddress: String, ipAssetIds: List<String>): Map<String, BigInteger>
    fun getIPAssetsSlashHistory(ipAssetIds: List<String>): Map<String, List<SlashRecord>>
}
