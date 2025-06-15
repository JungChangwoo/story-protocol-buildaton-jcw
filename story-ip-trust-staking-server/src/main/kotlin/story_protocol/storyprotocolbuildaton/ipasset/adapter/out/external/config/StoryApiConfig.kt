package story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.config

import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.okhttp.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import story_protocol.storyprotocolbuildaton.ipasset.adapter.out.external.client.StoryApiClient

@Configuration
class StoryApiConfig {

    @Bean
    fun storyApiClient(@Value("\${story.api.url}") baseUrl: String): StoryApiClient {
        return Feign.builder()
            .client(OkHttpClient())
            .encoder(JacksonEncoder())
            .decoder(JacksonDecoder())
            .target(StoryApiClient::class.java, baseUrl)
    }
}
