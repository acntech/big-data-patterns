package no.acntech.sandbox.pulsar

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.acntech.sandbox.pulsar.client.TwitterClient
import no.acntech.sandbox.pulsar.model.Tweet
import no.acntech.sandbox.pulsar.model.TweetRecord
import org.apache.pulsar.io.common.IOConfigUtils
import org.apache.pulsar.io.core.PushSource
import org.apache.pulsar.io.core.SourceContext
import org.apache.pulsar.io.core.annotations.Connector
import org.apache.pulsar.io.core.annotations.IOType
import org.slf4j.LoggerFactory
import java.time.Duration

@Connector(
        name = "acntech-twitter-source",
        type = IOType.SOURCE,
        help = "Connector to stream tweets from Twitter to Pulsar",
        configClass = TwitterSourceConfig::class
)
class TwitterSource : PushSource<Tweet>() {

    private var twitterClient: TwitterClient? = null

    override fun open(config: MutableMap<String, Any>?, sourceContext: SourceContext?) {
        runBlocking {
            LOGGER.info("Starting Twitter source connector")
            val sourceConfig = IOConfigUtils
                    .loadWithSecrets(config, TwitterSourceConfig::class.java, sourceContext)
            twitterClient = TwitterClient.builder()
                    .rootUrl("https://api.twitter.com")
                    .connectTimeout(Duration.ofMillis(sourceConfig.connectTimeout))
                    .bearerToken(sourceConfig.bearerToken)
                    .build()
            launch {
                twitterClient!!.getStream { tweet ->
                    consume(TweetRecord(tweet))
                }
            }
            LOGGER.info("Twitter source connector started")
        }
    }

    override fun close() {
        LOGGER.info("Closing Twitter source connector")
        LOGGER.info("Twitter source closed")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterSource::class.java)
    }
}