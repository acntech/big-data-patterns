package no.acntech.sandbox.pulsar.client

import no.acntech.sandbox.pulsar.model.AddStreamRule
import no.acntech.sandbox.pulsar.model.AddStreamRules
import no.acntech.sandbox.pulsar.model.DeleteStreamRules
import no.acntech.sandbox.pulsar.model.GetStreamRules
import no.acntech.sandbox.pulsar.model.GetTweet
import no.acntech.sandbox.pulsar.model.StreamRule
import no.acntech.sandbox.pulsar.model.Tweet
import org.slf4j.LoggerFactory
import java.time.Duration

class TwitterClient private constructor(
        private var rootUrl: String,
        private val restClient: RestClient
) {

    fun getStream(consumer: (tweet: Tweet) -> Unit) {
        val response = restClient.STREAM(
                "${rootUrl}/2/tweets/search/stream",
                { getTweet: GetTweet -> consumer(getTweet.data) },
                GetTweet::class.java
        )
        LOGGER.info("Completed! ${response.statusCode()}")
    }

    fun getStreamRules(): List<StreamRule> {
        val body = restClient.GET(
                "${rootUrl}/2/tweets/search/stream/rules",
                GetStreamRules.Response::class.java
        )
        return body.data
    }

    fun addStreamRules(rules: List<AddStreamRule>): List<StreamRule> {
        val body = restClient.POST(
                "${rootUrl}/2/tweets/search/stream/rules",
                AddStreamRules.Request(rules),
                AddStreamRules.Response::class.java
        )
        return body.data
    }

    fun deleteStreamRules(ids: List<String>): Int {
        val body = restClient.POST(
                "${rootUrl}/2/tweets/search/stream/rules",
                DeleteStreamRules.Request(DeleteStreamRules.Delete(ids)),
                DeleteStreamRules.Response::class.java
        )
        return body.meta.summary.deleted
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterClient::class.java)
        fun builder() = Builder()
    }

    data class Builder internal constructor(
            private var rootUrl: String? = null,
            private var connectTimeout: Duration = Duration.ofSeconds(5),
            private var bearerToken: String? = null
    ) {

        fun rootUrl(rootUrl: String) = apply { this.rootUrl = rootUrl }
        fun connectTimeout(connectTimeout: Duration) = apply { this.connectTimeout = connectTimeout }
        fun bearerToken(bearerToken: String) = apply { this.bearerToken = bearerToken }
        fun build(): TwitterClient {
            val restClient = RestClient.builder()
                    .connectTimeout(connectTimeout)
                    .bearerToken(bearerToken!!)
                    .build()
            return TwitterClient(rootUrl!!, restClient)
        }
    }
}