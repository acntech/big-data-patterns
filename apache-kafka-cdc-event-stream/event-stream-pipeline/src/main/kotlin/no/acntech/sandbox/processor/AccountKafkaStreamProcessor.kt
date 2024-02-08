package no.acntech.sandbox.processor

import no.acntech.sandbox.model.AccountEvent
import no.acntech.sandbox.model.EventEnvelope
import no.acntech.sandbox.model.EventKey
import no.acntech.sandbox.model.EventValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AccountKafkaStreamProcessor : KafkaStreamProcessor<EventEnvelope<EventKey>, EventEnvelope<EventValue<AccountEvent>>> {

    override fun process(key: EventEnvelope<EventKey>, value: EventEnvelope<EventValue<AccountEvent>>) {
        LOGGER.info("Received event $key with value $value")
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AccountKafkaStreamProcessor::class.java)
    }
}