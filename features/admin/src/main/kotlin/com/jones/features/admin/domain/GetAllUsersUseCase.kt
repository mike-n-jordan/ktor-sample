package com.jones.features.admin.domain

import com.jones.features.admin.domain.model.UserModel
import com.jones.features.admin.domain.model.toModel
import com.jones.features.user.route.UserResource
import com.jones.features.user.view.json.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.cookie

internal class GetAllUsersUseCase(private val client: HttpClient) {

    suspend fun execute(cookies: Map<String, String>): List<UserModel> {
        val usersRequest = client.get(UserResource.All()) {
            cookies.forEach { (key, value) -> cookie(key, value) }
        }
        val users = usersRequest.body<List<UserDto>>()
           .map { user -> user.toModel() }
        return users
    }
}
