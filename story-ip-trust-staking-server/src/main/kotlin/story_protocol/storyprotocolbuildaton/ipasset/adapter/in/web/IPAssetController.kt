package story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.IPAssetQueryRequest
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.dto.IPAssetsResponse
import story_protocol.storyprotocolbuildaton.ipasset.adapter.`in`.web.mapper.IPAssetWebMapper
import story_protocol.storyprotocolbuildaton.ipasset.application.port.`in`.IPAssetQueryUseCase

@RestController
@RequestMapping("/api/v3")
class IPAssetController(
    private val ipAssetQueryUseCase: IPAssetQueryUseCase,
    private val webMapper: IPAssetWebMapper
) {

    @PostMapping("/assets")
    fun getIPAssets(
        @RequestBody request: IPAssetQueryRequest
    ): ResponseEntity<IPAssetsResponse> {
        val query = webMapper.toDomain(request)
        val result = ipAssetQueryUseCase.getIPAssets(query)
        val response = webMapper.toResponse(result)
        
        return ResponseEntity.ok(response)
    }
}
