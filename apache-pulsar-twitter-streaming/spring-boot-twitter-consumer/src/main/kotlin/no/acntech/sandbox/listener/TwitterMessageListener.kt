package no.acntech.sandbox.listener

import no.acntech.sandbox.model.Tweet
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TwitterMessageListener : MessageListener<Tweet> {

    val LOGGER = LoggerFactory.getLogger(TwitterMessageListener::class.java);

    override fun received(consumer: Consumer<Tweet>?, message: Message<Tweet>?) {
        if (message == null) {
            LOGGER.info("Message is null")
        } else {
            val tweet = message.value
            LOGGER.info("New tweet, id: {}, text: {}", tweet.id, tweet.text)
        }
    }
}