package com.jones.features.admin.domain

import com.jones.features.admin.domain.model.UserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AdminService : KoinComponent {

    private val getUsersUseCase by inject<GetAllUsersUseCase>()

    suspend fun getAllUsers(cookies: Map<String, String>): List<UserModel> =
        getUsersUseCase.execute(cookies)
}
