package no.acntech.sandbox.consumer

interface PulsarConsumer<T> {

    fun consume(data: T)
}