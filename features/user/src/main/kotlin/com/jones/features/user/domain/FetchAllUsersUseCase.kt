package com.jones.features.user.domain

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.model.UserModel
import com.jones.features.user.domain.model.toModel

internal class FetchAllUsersUseCase(private val userRepository: UserRepository) {

    suspend fun execute(): List<UserModel> =
        userRepository.getAllUsers().map { it.toModel() }
}
