package no.acntech.sandbox.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.validation.constraints.NotEmpty

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.pulsar")
data class PulsarProperties(@NotEmpty val brokers: List<String>)
