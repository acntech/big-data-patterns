package no.acntech.sandbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@SpringBootApplication
class MastodonConsumerApplication

fun main(args: Array<String>) {
    runApplication<MastodonConsumerApplication>(*args)
}