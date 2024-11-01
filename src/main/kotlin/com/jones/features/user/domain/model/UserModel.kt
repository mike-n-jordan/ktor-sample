package com.jones.features.user.domain.model

import com.jones.features.user.data.entity.UserEntity
import com.jones.features.user.data.entity.UserTypeEntity


data class UserModel(
    val id: String,
    val username: String,
    val passwordHash: String,
    val type: UserType,
)

enum class UserType {
    USER, ADMIN
}

internal fun UserType.toEntity(): UserTypeEntity =
    when (this) {
        UserType.ADMIN -> UserTypeEntity.ADMIN
        UserType.USER -> UserTypeEntity.USER
    }

internal fun UserEntity.toModel(): UserModel =
    UserModel(
        id = id,
        username = username,
        passwordHash = passwordHash,
        type = UserType.USER,
    )

private fun UserTypeEntity.toModel(): UserType =
    when (this) {
        UserTypeEntity.ADMIN -> UserType.ADMIN
        UserTypeEntity.USER -> UserType.USER
    }
