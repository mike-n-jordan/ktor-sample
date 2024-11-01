package com.jones.features.user.domain.model

data class UserSessionModel(
    val userId: String,
    val token: String,
)
