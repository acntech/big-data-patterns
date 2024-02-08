package no.acntech.sandbox.config

import jakarta.validation.Valid
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@EnableConfigurationProperties(AppRestProperties::class)
@Configuration(proxyBeanMethods = false)
class RestConsumerConfig {

    @Bean
    fun mastodonRestClient(@Valid properties: AppRestProperties): RestClient {
        return RestClient.builder()
                .baseUrl(properties.consumer.mastodon.url.toString())
                .defaultHeaders { it.setBearerAuth(properties.consumer.mastodon.bearerToken!!) }
                .build()
    }
}