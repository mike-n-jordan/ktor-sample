package com.jones

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.resources.href
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.java.KoinJavaComponent
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

data class LiveApplicationContext(
    val koin: Koin,
) {

    inline fun <reified T : Any> href(resource: T): String {
        val application = koin.get<Application>()
        val urlBuilder = URLBuilder().apply {
            protocol = URLProtocol.HTTP
            host = "localhost"
            port = 8080
        }
        application.href(resource, urlBuilder)
        return urlBuilder.buildString()
    }

    inline fun ChromeOptions.useDriver(body: (ChromeDriver) -> Unit) {
        val driver = ChromeDriver(this)
        body(driver)
        driver.quit()
    }
}

suspend fun withLiveApplication(
    testBody: suspend LiveApplicationContext.() -> Unit
) {
    withContext(Dispatchers.IO) {
        val config = AppConfig(
            port = 8080,
            host = "localhost",
            dbUrl = "jdbc:sqlite::memory:",
            adminPassword = "admin",
        )
        val server = embeddedServer(
            Netty,
            port = config.port,
            host = config.host,
            module = { mainModule(config) }
        ).start(wait = false)

        delay(1000)

        val koin = KoinJavaComponent.getKoin()
        val context = LiveApplicationContext(koin)

        testBody(context)

        delay(1000)

        server.stop(0, 0)
    }
}
