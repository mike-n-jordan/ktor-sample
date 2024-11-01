package com.jones.features.user.domain

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.model.UserModel
import com.jones.features.user.domain.model.UserType
import com.jones.features.user.domain.model.toEntity
import com.jones.features.user.domain.model.toModel
import de.mkammerer.argon2.Argon2Factory

internal class CreateUserUseCase(private val userRepository: UserRepository) {

    suspend fun execute(username: String, password: CharArray, type: UserType): UserModel {
        val argon2 = Argon2Factory.create()
        val hashedPassword = argon2.hash(ITERATIONS, MEMORY, PARALLELISM, password)
        argon2.wipeArray(password)
        return userRepository.insertUser(username, hashedPassword, type.toEntity()).toModel()
    }

    companion object {
        private const val ITERATIONS = 10
        private const val MEMORY = 65536
        private const val PARALLELISM = 1
    }
}
