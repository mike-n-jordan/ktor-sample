package com.jones.features.user.view.json

import com.jones.features.user.domain.model.UserModel
import com.jones.features.user.domain.model.UserType
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val type: UserTypeDto
)

@Serializable
enum class UserTypeDto{
    ADMIN, USER;
}

internal fun UserTypeDto.toModel(): UserType =
    when (this) {
        UserTypeDto.ADMIN -> UserType.ADMIN
        UserTypeDto.USER -> UserType.USER
    }

internal fun UserModel.toDto(): UserDto =
    UserDto(
        id = id,
        username = username,
        type = type.toDto(),
    )

private fun UserType.toDto(): UserTypeDto =
    when (this) {
        UserType.ADMIN -> UserTypeDto.ADMIN
        UserType.USER -> UserTypeDto.USER
    }
