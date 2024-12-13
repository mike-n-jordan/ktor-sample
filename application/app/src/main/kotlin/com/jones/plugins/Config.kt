package com.jones.plugins

import com.jones.AppConfig
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

internal fun setupConfig(): AppConfig =
    ConfigLoaderBuilder.default()
        .addResourceSource("/application-staging.yml")
        .build()
        .loadConfigOrThrow<AppConfig>()
