package story_protocol.storyprotocolbuildaton

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["story_protocol"])
class StoryProtocolBuildatonApplication

fun main(args: Array<String>) {
    runApplication<StoryProtocolBuildatonApplication>(*args)
}
