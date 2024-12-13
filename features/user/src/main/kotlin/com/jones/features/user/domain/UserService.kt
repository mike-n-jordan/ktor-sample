package com.jones.features.user.domain

import com.jones.features.user.domain.model.UserModel
import com.jones.features.user.domain.model.UserSessionModel
import com.jones.features.user.domain.model.UserType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class UserService : KoinComponent {

    private val fetchUserUseCase by inject<FetchUserUseCase>()
    private val fetchAllUsersUseCase by inject<FetchAllUsersUseCase>()
    private val createSessionUseCase by inject<CreateSessionUseCase>()
    private val validateLoginUseCase by inject<ValidateLoginUseCase>()
    private val createUserUseCase by inject<CreateUserUseCase>()

    suspend fun createUser(username: String, password: CharArray, type: UserType): UserModel =
        createUserUseCase.execute(username, password, type)

    suspend fun createSessionFor(username: String, password: CharArray): UserSessionModel? {
        val user = validateLoginUseCase.execute(username, password)
            ?: return null
        return createSessionUseCase.execute(user.id)
    }

    suspend fun fetchUser(id: String): UserModel? =
        fetchUserUseCase.execute(id)

    suspend fun fetchAllUsers(): List<UserModel> =
        fetchAllUsersUseCase.execute()
}
