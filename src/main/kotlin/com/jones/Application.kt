package com.jones

import com.jones.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val config = setupConfig()
    embeddedServer(Netty, port = config.port, host = config.host, module = { mainModule(config) })
        .start(wait = true)
}

fun Application.mainModule(config: AppConfig) {
    configureKoin(config)
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    bootstrapAdmin(config)
    configureRouting()
}
