package no.acntech.sandbox.pulsar

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.acntech.sandbox.pulsar.client.TwitterClient
import no.acntech.sandbox.pulsar.model.AddStreamRule
import no.acntech.sandbox.pulsar.model.AddStreamRules
import no.acntech.sandbox.pulsar.model.DeleteStreamRules
import no.acntech.sandbox.pulsar.model.GetStreamRules
import no.acntech.sandbox.pulsar.model.StreamRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class TwitterClientTest {

    private var twitterClient: TwitterClient? = null
    private val objectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Test
    internal fun shallGetStreamRules() {
        val response = createMockResponse(
                GetStreamRules.Response(
                        listOf(
                                StreamRule("1", "star-wars", "star-wars-tag"),
                                StreamRule("2", "star-trek", "star-trek-tag")
                        ),
                        GetStreamRules.Meta("2022-10-10T12:00:00.000Z", 1)
                )
        )
        mockWebServer?.enqueue(response)
        val streamRules = twitterClient?.getStreamRules()
        assertThat(streamRules).hasSize(2)
    }

    @Test
    internal fun shallAddStreamRules() {
        val response = createMockResponse(
                AddStreamRules.Response(
                        listOf(
                                StreamRule("1", "star-wars", "star-wars-tag"),
                        ),
                        AddStreamRules.Meta(
                                "2022-10-10T12:00:00.000Z",
                                AddStreamRules.Summary(1, 0, 1, 0)
                        )
                )
        )
        mockWebServer?.enqueue(response)
        val addStreamRules = listOf(AddStreamRule("star-wars", "star-wars-tag"))
        val streamRules = twitterClient?.addStreamRules(addStreamRules)
        assertThat(streamRules).hasSize(1)
    }

    @Test
    internal fun shallDeleteStreamRules() {
        val response = createMockResponse(
                DeleteStreamRules.Response(
                        DeleteStreamRules.Meta(
                                "2022-10-10T12:00:00.000Z",
                                DeleteStreamRules.Summary(3, 0)
                        )
                )
        )
        mockWebServer?.enqueue(response)
        val ids = listOf("1", "2", "3")
        val deletes = twitterClient?.deleteStreamRules(ids)
        assertThat(deletes).isEqualTo(3)
    }

    @BeforeEach
    internal fun beforeEach() {
        twitterClient = TwitterClient.builder()
                .rootUrl("http://localhost:${mockWebServer?.port}")
                .bearerToken("super-secret-token")
                .build()
    }

    @AfterEach
    internal fun afterEach() {
    }

    private fun createMockResponse(body: Any): MockResponse {
        return MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(body))
    }

    companion object {

        var mockWebServer: MockWebServer? = null

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            mockWebServer = MockWebServer()
            mockWebServer!!.start()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            mockWebServer!!.shutdown()
        }
    }
}