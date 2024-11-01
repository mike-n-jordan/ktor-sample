package com.jones

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.accept
import io.ktor.client.request.head
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.java.KoinJavaComponent

class TestApplicationContext(
    val koin: Koin,
    val client: HttpClient,
    val application: Application,
    val cookieStorage: ClearableCookieStorage,
)

suspend fun withTestApplication(test: suspend TestApplicationContext.() -> Unit) {
    withContext(Dispatchers.IO) {
        testApplication {
            application {
                val config = AppConfig(
                    port = 8080,
                    host = "localhost",
                    dbUrl = "jdbc:sqlite::memory:",
                    adminPassword = "admin",
                )
                mainModule(config)
            }
            val cookieStorage = ClearableCookieStorage()
            val client = createClient {
                install(Resources)
                install(ContentNegotiation) {
                    json()
                }
                install(HttpCookies) {
                    storage = cookieStorage
                }

                followRedirects = false
                defaultRequest {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }
            client.head("/")
            val koin = KoinJavaComponent.getKoin()
            val context = TestApplicationContext(
                koin = koin,
                client = client,
                application = koin.get(),
                cookieStorage = cookieStorage,
            )
            test(context)
        }
    }
}
