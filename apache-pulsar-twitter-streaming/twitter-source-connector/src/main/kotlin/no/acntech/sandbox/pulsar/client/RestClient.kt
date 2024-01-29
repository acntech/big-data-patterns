package no.acntech.sandbox.pulsar.client

import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.Flow

internal class RestClient private constructor(
        private val client: HttpClient,
        private val bearerToken: String
) {

    @Suppress("FunctionName")
    fun <T> GET(url: String, bodyHandler: HttpResponse.BodyHandler<T>): HttpResponse<T> {
        val request = HttpRequest.newBuilder()
                .GET().uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer $bearerToken")
                .build()
        return client.send(request, bodyHandler)
    }

    @Suppress("FunctionName")
    fun <T> POST(url: String, body: String, bodyHandler: HttpResponse.BodyHandler<T>): HttpResponse<T> {
        val request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body)).uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer $bearerToken")
                .build()
        return client.send(request, bodyHandler)
    }

    @Suppress("FunctionName")
    fun STREAM(url: String, subscriber: Flow.Subscriber<String>): HttpResponse<Void> {
        val request = HttpRequest.newBuilder()
                .GET().uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer $bearerToken")
                .build()
        return client.send(request, HttpResponse.BodyHandlers.fromLineSubscriber(subscriber))
    }

    internal companion object {
        private val LOGGER = LoggerFactory.getLogger(RestClient::class.java)
        fun builder() = Builder()
    }

    internal data class Builder internal constructor(
            private var connectTimeout: Duration = Duration.ofSeconds(5),
            private var bearerToken: String? = null
    ) {

        fun connectTimeout(connectTimeout: Duration) = apply { this.connectTimeout = connectTimeout }
        fun bearerToken(bearerToken: String) = apply { this.bearerToken = bearerToken }
        fun build(): RestClient {
            val client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(connectTimeout)
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
            return RestClient(client, bearerToken!!)
        }
    }
}