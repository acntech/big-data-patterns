package no.acntech.sandbox.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.net.URL
import java.time.ZonedDateTime

data class AccountEvent(
        @JsonProperty("id") @NotBlank val id: String,
        @JsonProperty("url") @NotNull val url: URL,
        @JsonProperty("avatar") @NotNull val avatar: URL,
        @JsonProperty("username") @NotBlank val username: String,
        @JsonProperty("account") @NotBlank val account: String,
        @JsonProperty("display_name") @NotBlank val displayName: String,
        @JsonProperty("followers_count") @NotNull val followersCount: Int,
        @JsonProperty("following_count") @NotNull val followingCount: Int,
        @JsonProperty("statuses_count") @NotNull val statusesCount: Int,
        @JsonProperty("created_at") @NotNull val createdAt: ZonedDateTime
)
