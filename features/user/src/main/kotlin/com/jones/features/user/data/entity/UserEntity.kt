package com.jones.features.user.data.entity

data class UserEntity(
    val id: String,
    val username: String,
    val passwordHash: String,
    val type: UserTypeEntity,
)

enum class UserTypeEntity {
    USER, ADMIN
}
