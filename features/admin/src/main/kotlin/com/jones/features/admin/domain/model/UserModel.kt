package com.jones.features.admin.domain.model

import com.jones.features.user.view.json.UserDto
import com.jones.features.user.view.json.UserTypeDto
import kotlinx.serialization.Serializable

@Serializable
internal data class UserModel(
    val id: String,
    val username: String,
    val type: UserType,
)

internal enum class UserType {
    ADMIN,
    USER
}

internal fun UserDto.toModel() =
    UserModel(
        id = id,
        username = username,
        type = type.toModel()
    )

internal fun UserTypeDto.toModel() =
    when (this) {
        UserTypeDto.ADMIN -> UserType.ADMIN
        UserTypeDto.USER -> UserType.USER
    }
