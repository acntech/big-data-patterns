package no.acntech.sandbox.pulsar.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.util.concurrent.Flow

class JsonSubscriber<T>(
        private val objectMapper: ObjectMapper,
        private val consumer: (data: T) -> Unit,
        private val type: Class<T>
) : Flow.Subscriber<String> {

    private var counter: Long = 0;
    private var subscription: Flow.Subscription? = null

    override fun onSubscribe(subscription: Flow.Subscription?) {
        LOGGER.info("Started subscription to Twitter stream")
        this.subscription = subscription
        this.subscription!!.request(1)
    }

    override fun onNext(item: String?) {
        counter++
        if (item != null) {
            val data: T = objectMapper.readValue(item, type)
            this.consumer(data)
        } else {
            LOGGER.debug("Received empty tweet!")
        }
        if (counter % 100L == 0L) {
            LOGGER.info("Consumed $counter tweets")
        }
        this.subscription!!.request(1)
    }

    override fun onError(throwable: Throwable?) {
        LOGGER.error("Subscription failed", throwable)
    }

    override fun onComplete() {
        LOGGER.info("Completed subscription to Twitter stream")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JsonSubscriber::class.java)
    }
}