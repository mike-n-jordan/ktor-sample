package com.jones.security

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val userId: String, val token: String)
