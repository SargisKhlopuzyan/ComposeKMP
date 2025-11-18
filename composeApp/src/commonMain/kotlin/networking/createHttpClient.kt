package networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(engine: HttpClientEngine) : HttpClient {
    return HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    println(message)
                }

            }
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    // This is on case Json has unknown keys, after ignoring the app will not crash
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            basic {
                // TODO - Check video for Auth
//                bearer {
//                    loadTokens {}
//                    refreshTokens {}
//                }
            }
        }
    }
}