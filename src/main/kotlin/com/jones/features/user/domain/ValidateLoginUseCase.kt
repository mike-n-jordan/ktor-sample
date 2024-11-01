package com.jones.features.user.domain

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.model.UserModel
import com.jones.features.user.domain.model.toModel
import de.mkammerer.argon2.Argon2Factory

internal class ValidateLoginUseCase(private val userRepository: UserRepository) {

    suspend fun execute(username: String, password: CharArray): UserModel? {
        val user = userRepository.getUserByUsername(username)
            ?.toModel()
            ?: return null
        val argon = Argon2Factory.create()
        val isValid = argon.verify(user.passwordHash, password)
        argon.wipeArray(password)
        return user.takeIf { isValid }
    }
}
