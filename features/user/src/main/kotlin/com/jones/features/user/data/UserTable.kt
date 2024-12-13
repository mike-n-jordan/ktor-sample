package com.jones.features.user.data

import org.jetbrains.exposed.dao.id.IntIdTable

internal object UserTable : IntIdTable() {
    val username = varchar("username", 50)
    val passwordHash = varchar("password_hash", 255)
    val type = varchar("type", 50)
}

internal object UserSessionTable : IntIdTable() {
    val token = varchar("token", 50)
    val userId = reference("user_id", UserTable.id)
}
