package no.acntech.sandbox.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class SearchResultDto(
        @JsonProperty("accounts") @NotNull val accounts: List<AccountDto>,
        @JsonProperty("statuses") @NotNull val statuses: List<StatusDto>
)
