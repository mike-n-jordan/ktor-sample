package com.jones.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.*

fun database(dbUrl: String): Database {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = dbUrl
        minimumIdle = 1
    }
    val datasource = HikariDataSource(hikariConfig)
    Flyway.configure()
        .dataSource(datasource)
        .load()
        .migrate()
    return Database.connect(datasource)
}
