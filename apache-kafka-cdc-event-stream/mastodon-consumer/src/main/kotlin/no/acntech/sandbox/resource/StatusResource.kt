package no.acntech.sandbox.resource

import no.acntech.sandbox.consumer.MastodonRestConsumer
import no.acntech.sandbox.service.AccountService
import no.acntech.sandbox.service.StatusService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/api/statuses"])
@RestController
class StatusResource(private val accountService: AccountService,
                     private val statusService: StatusService,
                     private val mastodonRestConsumer: MastodonRestConsumer) {

    @PostMapping
    fun post(@RequestParam(name = "query", defaultValue = "tech") query: String,
             @RequestParam(name = "type", defaultValue = "statuses") type: String,
             @RequestParam(name = "offset", defaultValue = "0") offset: Int,
             @RequestParam(name = "limit", defaultValue = "10") limit: Int) {
        val responseEntity = mastodonRestConsumer.search(query, type, offset, limit)
        if (responseEntity.body == null) {
            LOGGER.info("No response body returned")
        } else if (responseEntity.body!!.statuses.isEmpty()) {
            LOGGER.warn("Response body contained no statuses")
        } else {
            responseEntity.body!!.statuses.forEach {
                accountService.upsert(it.account)
                statusService.upsert(it)
            }
        }
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(StatusResource::class.java)
    }
}