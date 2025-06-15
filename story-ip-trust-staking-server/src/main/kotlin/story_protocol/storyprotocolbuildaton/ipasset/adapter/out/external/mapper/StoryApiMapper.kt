package story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.mapper

import org.springframework.stereotype.Component
import story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.dto.*
import story_protocol.storyprotocolbuildaton.ipasset.domain.*

@Component
class StoryApiMapper {

    fun toStoryApiRequest(query: IPAssetQuery): StoryApiRequest {
        return StoryApiRequest(
            options = StoryApiQueryOptions(
                ipAssetIds = query.ipAssetIds,
                orderBy = query.orderBy?.let { mapOrderByToString(it) },
                orderDirection = query.orderDirection?.let { mapOrderDirectionToString(it) },
                pagination = query.pagination?.let { toStoryApiPagination(it) },
                tokenContractIds = query.tokenContractIds,
                tokenIds = query.tokenIds,
                where = query.where?.let { toStoryApiFilter(it) }
            )
        )
    }

    fun toDomain(response: StoryApiResponse): IPAssetsResult {
        return IPAssetsResult(
            data = response.data.map { toDomainIPAsset(it) },
            hasNextPage = response.hasNextPage,
            hasPreviousPage = response.hasPreviousPage,
            next = response.next,
            prev = response.prev
        )
    }

    private fun mapOrderByToString(orderBy: OrderBy): String {
        return when (orderBy) {
            OrderBy.ID -> "id"
            OrderBy.BLOCK_NUMBER -> "blockNumber"
            OrderBy.IP_ID -> "ipId"
            OrderBy.RESOURCE_TYPE -> "resourceType"
        }
    }

    private fun mapOrderDirectionToString(direction: OrderDirection): String {
        return when (direction) {
            OrderDirection.ASC -> "asc"
            OrderDirection.DESC -> "desc"
        }
    }

    private fun toStoryApiPagination(pagination: Pagination): StoryApiPagination {
        return StoryApiPagination(
            after = pagination.after,
            before = pagination.before,
            limit = pagination.limit
        )
    }

    private fun toStoryApiFilter(filter: IPAssetFilter): StoryApiFilter {
        return StoryApiFilter(
            blockNumber = filter.blockNumber,
            blockNumberGte = filter.blockNumberGte,
            blockNumberLte = filter.blockNumberLte,
            id = filter.id,
            ipId = filter.ipId,
            tokenContract = filter.tokenContract,
            tokenId = filter.tokenId
        )
    }

    private fun toDomainIPAsset(apiAsset: StoryApiIPAsset): IPAsset {
        return IPAsset.create(
            id = apiAsset.id,
            ipId = apiAsset.ipId,
            ancestorCount = apiAsset.ancestorCount,
            blockNumber = apiAsset.blockNumber,
            blockTimestamp = apiAsset.blockTimestamp,
            childrenCount = apiAsset.childrenCount,
            descendantCount = apiAsset.descendantCount,
            isGroup = apiAsset.isGroup,
            latestArbitrationPolicy = apiAsset.latestArbitrationPolicy,
            nftMetadata = apiAsset.nftMetadata?.let { toDomainNftMetadata(it) },
            parentCount = apiAsset.parentCount,
            rootCount = apiAsset.rootCount,
            rootIpIds = apiAsset.rootIpIds,
            transactionHash = apiAsset.transactionHash,
            trustRank = TrustRank.BRONZE,
            stakingInfo = StakingInfo.empty(),
        )
    }

    private fun toDomainNftMetadata(apiMetadata: StoryApiNftMetadata): NftMetadata {
        return NftMetadata(
            chainId = apiMetadata.chainId,
            imageUrl = apiMetadata.imageUrl,
            name = apiMetadata.name,
            tokenContract = apiMetadata.tokenContract,
            tokenId = apiMetadata.tokenId,
            tokenUri = apiMetadata.tokenUri
        )
    }
}
