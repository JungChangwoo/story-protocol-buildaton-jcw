package story_protocol.storyprotocolbuildaton.ipasset.application.port.out

import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetQuery
import story_protocol.storyprotocolbuildaton.ipasset.domain.IPAssetsResult

interface IPAssetRepository {
    fun findIPAssets(query: IPAssetQuery): IPAssetsResult
}
