package no.acntech.sandbox.pulsar.model

import org.apache.pulsar.functions.api.Record;
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TweetRecord(
        private val tweet: Tweet
) : Record<Tweet> {

    private val timestampFormat = SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy")

    override fun getKey(): Optional<String> {
        return Optional.of(tweet.id)
    }

    override fun getValue(): Tweet {
        return tweet;
    }

    override fun getEventTime(): Optional<Long> {
        return if (tweet.createdAt == null) {
            Optional.of(System.currentTimeMillis())
        } else {
            try {
                val date: Date = timestampFormat.parse(tweet.createdAt)
                Optional.of(date.toInstant().toEpochMilli());
            } catch (e: ParseException) {
                Optional.empty()
            }
        }
    }
}