package story_protocol.storyprotocolbuildaton.ipasset.domain

data class IPAsset(
    val id: IPAssetId,
    val ipId: String,
    val ancestorCount: Int,
    val blockNumber: String,
    val blockTimestamp: String,
    val childrenCount: Int,
    val descendantCount: Int,
    val isGroup: Boolean,
    val latestArbitrationPolicy: String?,
    val nftMetadata: NftMetadata?,
    val parentCount: Int,
    val rootCount: Int,
    val rootIpIds: List<String>,
    val transactionHash: String,
    val trustRank: TrustRank,
    val stakingInfo: StakingInfo
) {
    companion object {
        fun create(
            id: String,
            ipId: String,
            ancestorCount: Int,
            blockNumber: String,
            blockTimestamp: String,
            childrenCount: Int,
            descendantCount: Int,
            isGroup: Boolean,
            latestArbitrationPolicy: String?,
            nftMetadata: NftMetadata?,
            parentCount: Int,
            rootCount: Int,
            rootIpIds: List<String>,
            transactionHash: String,
            trustRank: TrustRank,
            stakingInfo: StakingInfo
        ): IPAsset {
            return IPAsset(
                id = IPAssetId(id),
                ipId = ipId,
                ancestorCount = ancestorCount,
                blockNumber = blockNumber,
                blockTimestamp = blockTimestamp,
                childrenCount = childrenCount,
                descendantCount = descendantCount,
                isGroup = isGroup,
                latestArbitrationPolicy = latestArbitrationPolicy,
                nftMetadata = nftMetadata,
                parentCount = parentCount,
                rootCount = rootCount,
                rootIpIds = rootIpIds,
                transactionHash = transactionHash,
                trustRank = trustRank,
                stakingInfo = stakingInfo
            )
        }
    }
}
