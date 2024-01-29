package no.acntech.sandbox.pulsar.model

import com.fasterxml.jackson.annotation.JsonProperty

class GetStreamRules {

    data class Response(
            val data: List<StreamRule> = emptyList(),
            val meta: Meta
    )

    data class Meta(
            val sent: String,
            @JsonProperty("result_count") val resultCount: Int,
    )
}
