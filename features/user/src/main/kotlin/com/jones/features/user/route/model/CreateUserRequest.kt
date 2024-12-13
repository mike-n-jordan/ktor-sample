package com.jones.features.user.route.model

import com.jones.features.user.view.json.UserTypeDto
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val username: String,
    val password: CharArray,
    val type: UserTypeDto,
)
