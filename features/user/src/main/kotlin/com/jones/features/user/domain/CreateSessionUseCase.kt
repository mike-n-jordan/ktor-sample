package com.jones.features.user.domain

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.model.UserSessionModel
import java.util.UUID

internal class CreateSessionUseCase(private val userRepository: UserRepository) {

    suspend fun execute(userId: String): UserSessionModel {
        val token = UUID.randomUUID().toString()
        userRepository.insertUserSession(userId, token)
        return UserSessionModel(userId, token)
    }
}
