package com.jones.features.admin

import com.jones.features.admin.domain.AdminService
import com.jones.features.admin.domain.GetAllUsersUseCase
import com.jones.features.admin.route.AdminController
import org.koin.dsl.module

fun adminModule() = module {
    single { AdminController(get()) }
    single { AdminService() }
    single { GetAllUsersUseCase(get()) }
}
