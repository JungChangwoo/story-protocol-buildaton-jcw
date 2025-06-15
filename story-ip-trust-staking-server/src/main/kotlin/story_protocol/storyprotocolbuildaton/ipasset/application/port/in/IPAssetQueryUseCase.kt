package story_protocol.storyprotocolbuildaton.ipasset.application.port.`in`

import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetQuery
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetsResult

interface IPAssetQueryUseCase {
    fun getIPAssets(query: IPAssetQuery): IPAssetsResult
}
