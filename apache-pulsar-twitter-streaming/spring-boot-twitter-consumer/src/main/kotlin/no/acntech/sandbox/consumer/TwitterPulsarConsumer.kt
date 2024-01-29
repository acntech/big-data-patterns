package no.acntech.sandbox.consumer

import no.acntech.sandbox.model.Subscriptions
import no.acntech.sandbox.model.Topics
import no.acntech.sandbox.model.Tweet
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.MessageListener
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.SubscriptionType
import org.apache.pulsar.client.impl.schema.JSONSchema
import org.springframework.stereotype.Component

@Component
class TwitterPulsarConsumer(
        pulsarClient: PulsarClient,
        messageListener: MessageListener<Tweet>
) {

    private val pulsarConsumer: Consumer<Tweet>;

    init {
        pulsarConsumer = pulsarClient.newConsumer(JSONSchema.of(Tweet::class.java))
                .topic(Topics.TWEETS)
                .subscriptionName(Subscriptions.TWITTER_CONSUMER)
                .subscriptionType(SubscriptionType.Shared)
                .messageListener(messageListener)
                .subscribe();
    }
}