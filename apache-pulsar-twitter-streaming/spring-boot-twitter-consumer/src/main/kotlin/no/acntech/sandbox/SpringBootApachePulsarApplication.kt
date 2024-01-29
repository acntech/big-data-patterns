package no.acntech.sandbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootApachePulsarApplication

fun main(args: Array<String>) {
    runApplication<SpringBootApachePulsarApplication>(*args)
}