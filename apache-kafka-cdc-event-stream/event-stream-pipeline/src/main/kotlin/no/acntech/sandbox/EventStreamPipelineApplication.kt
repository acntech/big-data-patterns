package no.acntech.sandbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventStreamPipelineApplication

fun main(args: Array<String>) {
    runApplication<EventStreamPipelineApplication>(*args)
}