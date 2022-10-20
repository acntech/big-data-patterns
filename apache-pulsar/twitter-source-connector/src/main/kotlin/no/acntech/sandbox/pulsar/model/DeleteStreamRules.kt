package no.acntech.sandbox.pulsar.model

import com.fasterxml.jackson.annotation.JsonProperty

class DeleteStreamRules {

    data class Request(
            val delete: Delete
    )

    data class Response(
            val meta: Meta
    )

    data class Delete(
            val ids: List<String>,
    )

    data class Meta(
            val sent: String,
            val summary: Summary
    )

    data class Summary(
            val deleted: Int,
            @JsonProperty("not_deleted") val notDeleted: Int,
    )
}
