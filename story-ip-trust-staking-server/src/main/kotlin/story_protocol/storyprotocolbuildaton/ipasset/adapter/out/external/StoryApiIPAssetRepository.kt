package story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.client.StoryApiClient
import story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.mapper.StoryApiMapper
import story_protocol.storyprotocolbuildaton.ipasset.application.port.out.IPAssetRepository
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetQuery
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetsResult

@Repository
class StoryApiIPAssetRepository(
    private val storyApiClient: StoryApiClient,
    private val storyApiMapper: StoryApiMapper,
    @Value("\${story.api.key}") private val apiKey: String,
    @Value("\${story.api.chain}") private val chain: String
) : IPAssetRepository {

    override fun findIPAssets(query: IPAssetQuery): IPAssetsResult {
        val request = storyApiMapper.toStoryApiRequest(query)
        val response = storyApiClient.getIPAssets(apiKey, chain, request)
        return storyApiMapper.toDomain(response)
    }
}
