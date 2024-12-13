package com.jones.plugins

import com.jones.AppConfig
import com.jones.features.user.data.UserRepository
import com.jones.features.user.data.entity.UserTypeEntity
import com.jones.features.user.domain.hashPassword
import io.ktor.server.application.Application
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

internal fun Application.bootstrapAdmin(config: AppConfig) {
    val userRepo by inject<UserRepository>()

    val admin = runBlocking {
        userRepo.getUserByUsername("admin")
    }

    if (admin == null) {
        if (config.adminPassword == null) {
            throw IllegalStateException("Admin password is not set for first run")
        }
        runBlocking {
            userRepo.insertUser("admin", config.adminPassword.toCharArray().hashPassword(), UserTypeEntity.ADMIN)
        }
    }
}
