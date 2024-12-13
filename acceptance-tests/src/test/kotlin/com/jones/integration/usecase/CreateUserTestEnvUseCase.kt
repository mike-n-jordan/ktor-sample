package com.jones.integration.usecase

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.hashPassword
import com.jones.features.user.view.json.UserTypeDto
import com.jones.features.user.view.json.toEntity

class CreateUserTestEnvUseCase(
    private val userRepository: UserRepository,
) {

    suspend fun execute(username: String, password: CharArray, type: UserTypeDto = UserTypeDto.USER) {
        val hashedPassword = password.hashPassword()
        userRepository.insertUser(username, hashedPassword, type.toEntity())
    }
}
