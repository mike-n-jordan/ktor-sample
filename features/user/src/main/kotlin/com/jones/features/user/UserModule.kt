package com.jones.features.user

import com.jones.features.user.data.UserRepository
import com.jones.features.user.domain.CreateSessionUseCase
import com.jones.features.user.domain.CreateUserUseCase
import com.jones.features.user.domain.FetchAllUsersUseCase
import com.jones.features.user.domain.FetchUserUseCase
import com.jones.features.user.domain.UserService
import com.jones.features.user.domain.ValidateLoginUseCase
import com.jones.features.user.route.UserController
import org.koin.dsl.module

fun userModule() = module {
    single { UserRepository(get()) }
    single { UserService() }
    single { UserController(get()) }
    single { FetchUserUseCase(get()) }
    single { FetchAllUsersUseCase(get()) }
    single { CreateSessionUseCase(get()) }
    single { ValidateLoginUseCase(get()) }
    single { CreateUserUseCase(get()) }
}
