package com.jones.plugins

import com.jones.AppConfig
import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.CreateUserUseCase
import com.jones.features.user.domain.model.UserType
import io.ktor.server.application.Application
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

fun Application.bootstrapAdmin(config: AppConfig) {
    val userRepo by inject<UserRepository>()
    val createUserUseCase by inject<CreateUserUseCase>()

    val admin = runBlocking {
        userRepo.getUserByUsername("admin")
    }

    if (admin == null) {
        if (config.adminPassword == null) {
            throw IllegalStateException("Admin password is not set for first run")
        }
        runBlocking {
            createUserUseCase.execute("admin", config.adminPassword.toCharArray(), UserType.ADMIN)
        }
    }
}
