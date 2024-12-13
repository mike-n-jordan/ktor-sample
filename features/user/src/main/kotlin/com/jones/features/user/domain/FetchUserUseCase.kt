package com.jones.features.user.domain

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.model.UserModel
import com.jones.features.user.domain.model.toModel

internal class FetchUserUseCase(private val userRepository: UserRepository) {

    suspend fun execute(userId: String): UserModel? =
        userRepository.getUserById(userId)?.toModel()
}
