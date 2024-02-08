package no.acntech.sandbox.consumer

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import no.acntech.sandbox.model.SearchResultDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class MastodonRestConsumer(@Qualifier("mastodonRestClient") private val client: RestClient) {

    fun search(@NotBlank query: String,
               @NotBlank type: String,
               @NotNull offset: Int = 0,
               @NotNull limit: Int = 10): ResponseEntity<SearchResultDto> {
        return client.get()
                .uri {
                    it.path("/api/v2/search")
                            .queryParam("q", query)
                            .queryParam("type", type)
                            .queryParam("offset", offset)
                            .queryParam("limit", limit)
                            .build()
                }
                .retrieve()
                .toEntity(SearchResultDto::class.java)
    }
}