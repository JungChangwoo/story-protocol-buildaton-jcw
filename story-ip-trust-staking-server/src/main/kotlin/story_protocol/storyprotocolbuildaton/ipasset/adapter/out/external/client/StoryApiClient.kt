package story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.client

import feign.Headers
import feign.Param
import feign.RequestLine
import story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.dto.StoryApiRequest
import story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.dto.StoryApiResponse

interface StoryApiClient {
    
    @RequestLine("POST /api/v3/assets")
    @Headers(
        "Content-Type: application/json",
        "X-Api-Key: {apiKey}",
        "X-Chain: {chain}"
    )
    fun getIPAssets(
        @Param("apiKey") apiKey: String,
        @Param("chain") chain: String,
        request: StoryApiRequest
    ): StoryApiResponse
}
