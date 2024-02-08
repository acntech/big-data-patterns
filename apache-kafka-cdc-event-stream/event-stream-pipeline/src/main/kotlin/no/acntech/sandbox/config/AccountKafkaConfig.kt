package no.acntech.sandbox.config

import com.fasterxml.jackson.databind.ObjectMapper
import no.acntech.sandbox.model.AccountEvent
import no.acntech.sandbox.model.EventEnvelope
import no.acntech.sandbox.model.EventKey
import no.acntech.sandbox.model.EventValue
import no.acntech.sandbox.processor.AccountKafkaStreamProcessor
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
class AccountKafkaConfig {

    init {
        LOGGER.info("Applying ${this.javaClass.name}")
    }

    @Bean
    fun accountEventValueSerde(objectMapper: ObjectMapper): JsonSerde<EventEnvelope<EventValue<AccountEvent>>> {
        val eventValueType = objectMapper.typeFactory.constructParametricType(EventValue::class.java, AccountEvent::class.java)
        val eventEnvelopeType = objectMapper.typeFactory.constructParametricType(EventEnvelope::class.java, eventValueType)
        return JsonSerde<EventEnvelope<EventValue<AccountEvent>>>(eventEnvelopeType, objectMapper)
    }

    @Bean
    fun accountKafkaConsumerFactory(kafkaProperties: KafkaProperties,
                                    sslBundles: ObjectProvider<SslBundles>):
            ConsumerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<AccountEvent>>> {
        return DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties(sslBundles.getIfAvailable()))
    }

    @Bean
    fun accountKafkaListenerContainerFactory(
            @Qualifier("accountKafkaConsumerFactory") consumerFactory: ConsumerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<AccountEvent>>>):
            ConcurrentKafkaListenerContainerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<AccountEvent>>> {
        val factory = ConcurrentKafkaListenerContainerFactory<EventEnvelope<EventKey>, EventEnvelope<EventValue<AccountEvent>>>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    @Bean
    fun accountKafkaStreamsTopology(streamsBuilder: StreamsBuilder,
                                    properties: AppKafkaProperties,
                                    @Qualifier("eventKeySerde") keySerde: JsonSerde<EventEnvelope<EventKey>>,
                                    @Qualifier("accountEventValueSerde") valueSerde: JsonSerde<EventEnvelope<EventValue<AccountEvent>>>,
                                    accountKafkaStreamProcessor: AccountKafkaStreamProcessor): Topology {
        val statusKafkaStream = streamsBuilder.stream(properties.stream.accountSourceTopic, Consumed.with(keySerde, valueSerde))

        statusKafkaStream.foreach(accountKafkaStreamProcessor::process)

        LOGGER.info("Created Status Kafka Streams Topology")

        return streamsBuilder.build()
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AccountKafkaConfig::class.java)
    }
}