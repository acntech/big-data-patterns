package no.acntech.sandbox.processor

import no.acntech.sandbox.model.EventEnvelope
import no.acntech.sandbox.model.EventKey
import no.acntech.sandbox.model.EventValue
import no.acntech.sandbox.model.StatusEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StatusKafkaStreamProcessor : KafkaStreamProcessor<EventEnvelope<EventKey>, EventEnvelope<EventValue<StatusEvent>>> {

    override fun process(key: EventEnvelope<EventKey>, value: EventEnvelope<EventValue<StatusEvent>>) {
        LOGGER.info("Received event $key with value $value")
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(StatusKafkaStreamProcessor::class.java)
    }
}