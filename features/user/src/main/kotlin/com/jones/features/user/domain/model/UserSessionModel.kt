package com.jones.features.user.domain.model

internal data class UserSessionModel(
    val userId: String,
    val token: String,
)
