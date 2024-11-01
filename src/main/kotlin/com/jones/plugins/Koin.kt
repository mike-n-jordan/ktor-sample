package com.jones.plugins

import com.jones.AppConfig
import com.jones.features.user.userModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(config: AppConfig) {
    install(Koin) {
        modules(
            baseModule(config),
            userModule(),
        )
    }
}

private fun Application.baseModule(config: AppConfig) = module {
    single { config }
    single(createdAtStart = true) { database(config.dbUrl) }
    single<Application> { this@baseModule }
}
