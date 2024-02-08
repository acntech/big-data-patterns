package no.acntech.sandbox.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class EventEnvelope<T>(@JsonProperty("payload") @NotNull val payload: T)
