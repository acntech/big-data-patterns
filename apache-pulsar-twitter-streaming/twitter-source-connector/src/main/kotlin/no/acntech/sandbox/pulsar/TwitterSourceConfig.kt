package no.acntech.sandbox.pulsar

import com.fasterxml.jackson.annotation.JsonProperty

data class TwitterSourceConfig(
        @JsonProperty("connect_timeout") val connectTimeout: Long = 5000,
        @JsonProperty("bearer_token") val bearerToken: String
)
