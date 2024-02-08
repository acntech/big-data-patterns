package no.acntech.sandbox.config

import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.pulsar")
data class PulsarProperties(@NotEmpty val brokers: List<String>)
