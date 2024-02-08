package no.acntech.sandbox.config

import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URL

@ConfigurationProperties(prefix = "app.rest")
data class AppRestProperties(@NotNull val consumer: AppConsumerProperties)

data class AppConsumerProperties(
        @NotNull val mastodon: AppClientProperties
)

data class AppClientProperties(
        @NotNull val url: URL,
        val bearerToken: String?
)
