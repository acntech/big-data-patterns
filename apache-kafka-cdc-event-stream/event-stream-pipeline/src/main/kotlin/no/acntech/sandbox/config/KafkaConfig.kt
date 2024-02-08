package no.acntech.sandbox.config

import com.fasterxml.jackson.databind.ObjectMapper
import no.acntech.sandbox.model.EventEnvelope
import no.acntech.sandbox.model.EventKey
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.support.serializer.JsonSerde

@EnableConfigurationProperties(AppKafkaProperties::class)
@EnableKafkaStreams
@EnableKafka
@Configuration(proxyBeanMethods = false)
class KafkaConfig {

    init {
        LOGGER.info("Applying ${this.javaClass.name}")
    }

    @Bean
    fun stringSerde(): Serde<String> {
        return Serdes.String()
    }

    @Bean
    fun eventKeySerde(objectMapper: ObjectMapper): JsonSerde<EventEnvelope<EventKey>> {
        val type = objectMapper.typeFactory.constructParametricType(EventEnvelope::class.java, EventKey::class.java)
        return JsonSerde<EventEnvelope<EventKey>>(type, objectMapper)
    }

    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(KafkaConfig::class.java)
    }
}