package com.jones

data class AppConfig(
    val port: Int,
    val host: String,
    val dbUrl: String,
    val adminPassword: String?,
)
