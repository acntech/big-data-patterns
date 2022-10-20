package no.acntech.sandbox.pulsar.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

internal class RestClient private constructor(
        private val client: HttpClient,
        private val bearerToken: String
) {

    private val objectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Suppress("FunctionName")
    fun <T> GET(url: String, returnType: Class<T>): T {
        val request = HttpRequest.newBuilder()
                .GET().uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer $bearerToken")
                .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return objectMapper.readValue(response.body(), returnType)
    }

    @Suppress("FunctionName")
    fun <T> POST(url: String, body: Any, returnType: Class<T>): T {
        val bodyString = objectMapper.writeValueAsString(body)
        val request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(bodyString)).uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer $bearerToken")
                .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return objectMapper.readValue(response.body(), returnType)
    }

    @Suppress("FunctionName")
    fun <T> STREAM(url: String, consumer: (data: T) -> Unit, returnType: Class<T>): HttpResponse<Void> {
        val request = HttpRequest.newBuilder()
                .GET().uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer $bearerToken")
                .build()
        val subscriber = JsonSubscriber(objectMapper, consumer, returnType)
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