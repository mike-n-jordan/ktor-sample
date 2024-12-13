package com.jones.features.user.data

import com.jones.features.user.data.entity.UserEntity
import com.jones.features.user.data.entity.UserTypeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository(private val database: Database) {

    suspend fun insertUser(username: String, passwordHash: String, type: UserTypeEntity): UserEntity =
        query {
            val id = UserTable.insertAndGetId {
                it[UserTable.username] = username
                it[UserTable.passwordHash] = passwordHash
                it[UserTable.type] = type.name
            }.value.toString()

            UserEntity(id, username, passwordHash, type)
        }

    suspend fun getAllUsers(): List<UserEntity> =
        query {
            UserTable
                .selectAll()
                .map { it.toUserEntity() }
        }

    suspend fun getUserById(id: String): UserEntity? =
        query {
            UserTable
                .select(UserTable.columns)
                .where { UserTable.id eq id.toInt() }
                .singleOrNull()
                ?.toUserEntity()
        }

    suspend fun getUserByUsername(username: String): UserEntity? =
        query {
            UserTable
                .select(UserTable.columns)
                .where { UserTable.username eq username }
                .singleOrNull()
                ?.toUserEntity()
        }

    suspend fun getUserBySession(session: String): UserEntity? =
        query {
            UserSessionTable
                .join(UserTable, JoinType.FULL, onColumn = UserSessionTable.userId, otherColumn = UserTable.id)
                .select(UserTable.columns)
                .where { UserSessionTable.token eq session }
                .singleOrNull()
                ?.toUserEntity()
        }

    suspend fun insertUserSession(userId: String, token: String) {
        query {
            UserSessionTable.insert {
                it[UserSessionTable.userId] = userId.toInt()
                it[UserSessionTable.token] = token
            }
        }
    }

    private fun ResultRow.toUserEntity(): UserEntity =
        UserEntity(
            id = this[UserTable.id].value.toString(),
            username = this[UserTable.username],
            passwordHash = this[UserTable.passwordHash],
            type = UserTypeEntity.valueOf(this[UserTable.type]),
        )

    private suspend fun <T> query(body: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction(database) {
                body()
            }
        }
}
