package no.acntech.sandbox.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.kafka")
data class AppKafkaProperties(val stream: AppKafkaStreamProperties)

data class AppKafkaStreamProperties(
        @NotBlank val accountSourceTopic: String,
        @NotBlank val statusSourceTopic: String
)
