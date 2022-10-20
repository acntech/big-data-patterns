package no.acntech.sandbox.config

import org.apache.pulsar.client.api.PulsarClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.validation.Valid

@EnableConfigurationProperties(PulsarProperties::class)
@Configuration(proxyBeanMethods = false)
class PulsarConfig {

    @Bean
    fun pulsarClient(@Valid properties: PulsarProperties): PulsarClient {
        return PulsarClient.builder()
            .serviceUrl("pulsar://" + properties.brokers.joinToString(separator = ","))
            .build();
    }
}