package story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.mapper

import org.springframework.stereotype.Component
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.IPAssetDto
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.IPAssetFilterRequest
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.IPAssetQueryRequest
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.IPAssetsResponse
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.NftMetadataDto
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.PaginationRequest
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.StakingInfoDto
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.SlashRecordDto
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAsset
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetFilter
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetQuery
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetsResult
import story_protocol.storyprotocolbuildaton.ipasset.domain.NftMetadata
import story_protocol.storyprotocolbuildaton.ipasset.domain.OrderBy
import story_protocol.storyprotocolbuildaton.ipasset.domain.OrderDirection
import story_protocol.storyprotocolbuildaton.ipasset.domain.Pagination
import story_protocol.storyprotocolbuildaton.ipasset.domain.StakingInfo
import story_protocol.storyprotocolbuildaton.ipasset.domain.SlashRecord

@Component
class IPAssetWebMapper {

    fun toDomain(request: IPAssetQueryRequest): IPAssetQuery {
        return IPAssetQuery(
            ipAssetIds = request.options.ipAssetIds,
            orderBy = request.options.orderBy?.let { mapOrderBy(it) },
            orderDirection = request.options.orderDirection?.let { mapOrderDirection(it) },
            pagination = request.options.pagination?.let { toDomainPagination(it) },
            tokenContractIds = request.options.tokenContractIds,
            tokenIds = request.options.tokenIds,
            where = request.options.where?.let { toDomainFilter(it) },
            userAddress = request.userAddress
        )
    }

    fun toResponse(result: IPAssetsResult): IPAssetsResponse {
        return IPAssetsResponse(
            data = result.data.map { toDto(it) },
            hasNextPage = result.hasNextPage,
            hasPreviousPage = result.hasPreviousPage,
            next = result.next,
            prev = result.prev
        )
    }

    private fun mapOrderBy(orderBy: String): OrderBy {
        return when (orderBy.lowercase()) {
            "id" -> OrderBy.ID
            "blocknumber" -> OrderBy.BLOCK_NUMBER
            "ipid" -> OrderBy.IP_ID
            "resourcetype" -> OrderBy.RESOURCE_TYPE
            else -> OrderBy.ID
        }
    }

    private fun mapOrderDirection(direction: String): OrderDirection {
        return when (direction.lowercase()) {
            "asc" -> OrderDirection.ASC
            "desc" -> OrderDirection.DESC
            else -> OrderDirection.ASC
        }
    }

    private fun toDomainPagination(pagination: PaginationRequest): Pagination {
        return Pagination(
            after = pagination.after,
            before = pagination.before,
            limit = pagination.limit
        )
    }

    private fun toDomainFilter(filter: IPAssetFilterRequest): IPAssetFilter {
        return IPAssetFilter(
            blockNumber = filter.blockNumber,
            blockNumberGte = filter.blockNumberGte,
            blockNumberLte = filter.blockNumberLte,
            id = filter.id,
            ipId = filter.ipId,
            tokenContract = filter.tokenContract,
            tokenId = filter.tokenId
        )
    }

    private fun toDto(ipAsset: IPAsset): IPAssetDto {
        return IPAssetDto(
            ancestorCount = ipAsset.ancestorCount,
            blockNumber = ipAsset.blockNumber,
            blockTimestamp = ipAsset.blockTimestamp,
            childrenCount = ipAsset.childrenCount,
            descendantCount = ipAsset.descendantCount,
            id = ipAsset.id.value,
            ipId = ipAsset.ipId,
            isGroup = ipAsset.isGroup,
            latestArbitrationPolicy = ipAsset.latestArbitrationPolicy,
            nftMetadata = ipAsset.nftMetadata?.let { toNftMetadataDto(it) },
            parentCount = ipAsset.parentCount,
            rootCount = ipAsset.rootCount,
            rootIpIds = ipAsset.rootIpIds,
            transactionHash = ipAsset.transactionHash,
            trustRank = ipAsset.trustRank.name,
            stakingInfo = toStakingInfoDto(ipAsset.stakingInfo)
        )
    }

    private fun toNftMetadataDto(nftMetadata: NftMetadata): NftMetadataDto {
        return NftMetadataDto(
            chainId = nftMetadata.chainId,
            imageUrl = nftMetadata.imageUrl,
            name = nftMetadata.name,
            tokenContract = nftMetadata.tokenContract,
            tokenId = nftMetadata.tokenId,
            tokenUri = nftMetadata.tokenUri
        )
    }

    private fun toStakingInfoDto(stakingInfo: StakingInfo): StakingInfoDto {
        return StakingInfoDto(
            totalStakedAmount = stakingInfo.totalStakedAmount.toString(),
            userStakedAmount = stakingInfo.userStakedAmount?.toString(),
            slashHistory = stakingInfo.slashHistory.map { toSlashRecordDto(it) }
        )
    }

    private fun toSlashRecordDto(slashRecord: SlashRecord): SlashRecordDto {
        return SlashRecordDto(
            slashedIPAsset = slashRecord.slashedIPAsset,
            redistributedToIPAsset = slashRecord.redistributedToIPAsset,
            amount = slashRecord.amount.toString(),
            timestamp = slashRecord.timestamp
        )
    }
}
