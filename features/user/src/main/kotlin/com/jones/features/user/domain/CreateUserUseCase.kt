package com.jones.features.user.domain

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.model.UserModel
import com.jones.features.user.domain.model.UserType
import com.jones.features.user.domain.model.toEntity
import com.jones.features.user.domain.model.toModel

internal class CreateUserUseCase(private val userRepository: UserRepository) {

    suspend fun execute(username: String, password: CharArray, type: UserType): UserModel {
        val hashedPassword = password.hashPassword()
        return userRepository.insertUser(username, hashedPassword, type.toEntity()).toModel()
    }
}
