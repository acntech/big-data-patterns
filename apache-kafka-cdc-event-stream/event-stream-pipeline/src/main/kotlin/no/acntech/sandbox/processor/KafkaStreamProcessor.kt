package no.acntech.sandbox.processor

interface KafkaStreamProcessor<K, V> {

    fun process(key: K, value: V)
}