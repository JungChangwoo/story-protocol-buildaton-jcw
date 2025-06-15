package story_protocol.storyprotocolbuildaton.ipasset.application.service

import org.springframework.stereotype.Service
import story_protocol.storyprotocolbuildaton.ipasset.application.port.`in`.IPAssetQueryUseCase
import story_protocol.storyprotocolbuildaton.ipasset.application.port.out.IPAssetRepository
import story_protocol.storyprotocolbuildaton.ipasset.application.port.out.StakingContractRepository
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetQuery
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetsResult
import story_protocol.storyprotocolbuildaton.ipasset.domain.StakingInfo
import story_protocol.storyprotocolbuildaton.ipasset.domain.TrustRank

@Service
class IPAssetQueryService(
    private val ipAssetRepository: IPAssetRepository,
    private val stakingContractRepository: StakingContractRepository
) : IPAssetQueryUseCase {

    override fun getIPAssets(query: IPAssetQuery): IPAssetsResult {
        val result = ipAssetRepository.findIPAssets(query)
        if (result.data.isEmpty()) {
            return result
        }

        val ipAssetIds = result.data.map { it.ipId }
        val totalStakedAmounts = stakingContractRepository.getTotalStakedAmounts(ipAssetIds)
        val userStakedAmounts = query.userAddress?.let { userAddress ->
            stakingContractRepository.getUserStakedAmounts(userAddress, ipAssetIds)
        } ?: emptyMap()
        val slashHistories = stakingContractRepository.getIPAssetsSlashHistory(ipAssetIds)

        val enrichedAssets = result.data.map { ipAsset ->
            val totalStakedAmount = totalStakedAmounts[ipAsset.ipId] ?: java.math.BigInteger.ZERO
            val userStakedAmount = userStakedAmounts[ipAsset.ipId]
            val slashHistory = slashHistories[ipAsset.ipId] ?: emptyList()
            val trustRank = TrustRank.fromStakedAmount(stakedAmount = totalStakedAmount)
            val stakingInfo = StakingInfo.create(totalStakedAmount, userStakedAmount, slashHistory)
            ipAsset.copy(trustRank = trustRank, stakingInfo = stakingInfo)
        }
        
        return result.copy(data = enrichedAssets)
    }
}
