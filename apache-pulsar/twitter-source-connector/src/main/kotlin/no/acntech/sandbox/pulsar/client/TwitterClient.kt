package no.acntech.sandbox.pulsar.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.acntech.sandbox.pulsar.model.AddStreamRule
import no.acntech.sandbox.pulsar.model.AddStreamRules
import no.acntech.sandbox.pulsar.model.DeleteStreamRules
import no.acntech.sandbox.pulsar.model.GetStreamRules
import no.acntech.sandbox.pulsar.model.GetTweet
import no.acntech.sandbox.pulsar.model.StreamRule
import no.acntech.sandbox.pulsar.model.Tweet
import org.slf4j.LoggerFactory
import java.net.http.HttpResponse
import java.time.Duration

class TwitterClient private constructor(
        private var rootUrl: String,
        private val restClient: RestClient
) {

    private val objectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getStream(consumer: (tweet: Tweet) -> Unit): Int {
        LOGGER.info("Starting Twitter stream consumer")
        val bodyConsumer = { getTweet: GetTweet -> consumer(getTweet.data) }
        val subscriber = JsonSubscriber(objectMapper, bodyConsumer, GetTweet::class.java)
        val response = restClient.STREAM("${rootUrl}/2/tweets/search/stream", subscriber)
        LOGGER.info("Twitter stream consumer completed with status ${response.statusCode()}")
        return response.statusCode()
    }

    fun getStreamRules(): List<StreamRule> {
        LOGGER.info("Getting Twitter stream rules")
        val response = restClient.GET("${rootUrl}/2/tweets/search/stream/rules", HttpResponse.BodyHandlers.ofString())
        val body = objectMapper.readValue(response.body(), GetStreamRules.Response::class.java)
        return body.data
    }

    fun addStreamRules(rules: List<AddStreamRule>): List<StreamRule> {
        LOGGER.info("Adding Twitter stream rules")
        val bodyString = objectMapper.writeValueAsString(AddStreamRules.Request(rules))
        val response = restClient.POST("${rootUrl}/2/tweets/search/stream/rules", bodyString, HttpResponse.BodyHandlers.ofString())
        val body = objectMapper.readValue(response.body(), AddStreamRules.Response::class.java)
        return body.data
    }

    fun deleteStreamRules(ids: List<String>): Int {
        LOGGER.info("Deleting Twitter stream rules")
        val bodyString = objectMapper.writeValueAsString(DeleteStreamRules.Request(DeleteStreamRules.Delete(ids)))
        val response = restClient.POST("${rootUrl}/2/tweets/search/stream/rules", bodyString, HttpResponse.BodyHandlers.ofString())
        val body = objectMapper.readValue(response.body(), DeleteStreamRules.Response::class.java)
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