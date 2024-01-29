package no.acntech.sandbox.pulsar.model

import com.fasterxml.jackson.annotation.JsonProperty

class AddStreamRules {

    data class Request(
            val add: List<AddStreamRule>
    )

    data class Response(
            val data: List<StreamRule> = emptyList(),
            val meta: Meta
    )

    data class Meta(
            val sent: String,
            val summary: Summary
    )

    data class Summary(
            val created: Int,
            @JsonProperty("not_created") val notCreated: Int,
            val valid: Int,
            val invalid: Int
    )
}
