package no.acntech.sandbox.config

import com.fasterxml.jackson.databind.ObjectMapper
import no.acntech.sandbox.model.EventEnvelope
import no.acntech.sandbox.model.EventKey
import no.acntech.sandbox.model.EventValue
import no.acntech.sandbox.model.StatusEvent
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.ssl.SslBundles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonSerde


@Configuration(proxyBeanMethods = false)
class StatusKafkaConfig {

    init {
        LOGGER.info("Applying ${this.javaClass.name}")
    }

    @Bean
    fun statusEventValueSerde(objectMapper: ObjectMapper): JsonSerde<EventEnvelope<EventValue<StatusEvent>>> {
        val eventValueType = objectMapper.typeFactory.constructParametricType(EventValue::class.java, StatusEvent::class.java)
        val eventEnvelopeType = objectMapper.typeFactory.constructParametricType(EventEnvelope::class.java, eventValueType)
        return JsonSerde<EventEnvelope<EventValue<StatusEvent>>>(eventEnvelopeType, objectMapper)
    }

    @Bean
    fun statusKafkaConsumerFactory(kafkaProperties: KafkaProperties,
                                   sslBundles: ObjectProvider<SslBundles>):
            ConsumerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<StatusEvent>>> {
        return DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties(sslBundles.getIfAvailable()))
    }

    @Bean
    fun statusKafkaListenerContainerFactory(
            @Qualifier("statusKafkaConsumerFactory") consumerFactory: ConsumerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<StatusEvent>>>):
            ConcurrentKafkaListenerContainerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<StatusEvent>>> {
        val factory = ConcurrentKafkaListenerContainerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<StatusEvent>>>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    @Bean
    fun statusKafkaStreamsTopology(streamsBuilder: StreamsBuilder,
                                   properties: AppKafkaProperties,
                                   @Qualifier("eventKeySerde") keySerde: JsonSerde<EventEnvelope<EventKey>>,
                                   @Qualifier("statusEventValueSerde") valueSerde: JsonSerde<EventEnvelope<EventValue<StatusEvent>>>): Topology {
        val statusKafkaStream = streamsBuilder.stream(properties.stream.statusSourceTopic, Consumed.with(keySerde, valueSerde))

        statusKafkaStream
                .filter { key, value -> key != null && value != null }
                .foreach { key, value -> LOGGER.info("Received Status Change Event $key with value $value") }

        LOGGER.info("Created Status Kafka Streams Topology")

        return streamsBuilder.build()
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(StatusKafkaConfig::class.java)
    }
}