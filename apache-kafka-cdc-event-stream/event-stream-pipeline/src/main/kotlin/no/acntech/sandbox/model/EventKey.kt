package no.acntech.sandbox.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class EventKey(@JsonProperty("id") @NotBlank val id: String)
