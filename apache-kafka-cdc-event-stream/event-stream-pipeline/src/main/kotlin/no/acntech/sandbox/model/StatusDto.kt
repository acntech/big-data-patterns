package no.acntech.sandbox.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.net.URL
import java.time.ZonedDateTime

data class StatusDto(
        @JsonProperty("id") @NotBlank val id: String,
        @JsonProperty("url") @NotNull val url: URL,
        @JsonProperty("language") @NotBlank val language: String,
        @JsonProperty("visibility") @NotBlank val visibility: String,
        @JsonProperty("content") @NotBlank val content: String,
        @JsonProperty("in_reply_to_id") val inReplyToStatusId: String?,
        @JsonProperty("in_reply_to_account_id") val inReplyToAccountId: String?,
        @JsonProperty("replies_count") @NotNull val repliesCount: Int,
        @JsonProperty("reblogs_count") @NotNull val reblogsCount: Int,
        @JsonProperty("favourites_count") @NotNull val favouritesCount: Int,
        @JsonProperty("created_at") @NotNull val createdAt: ZonedDateTime,
        @JsonProperty("account") @NotNull val account: AccountDto
)
