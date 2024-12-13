package com.jones.plugins

import com.jones.features.user.data.UserRepository
import com.jones.features.user.data.entity.UserTypeEntity
import com.jones.features.user.route.UserResource
import com.jones.security.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.resources.href
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import org.koin.ktor.ext.get
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Security")

internal fun Application.configureSecurity() {
    install(Authentication) {
        val userRepo: UserRepository = this@configureSecurity.get()
        session<UserSession>("auth-session") {
            validate { session ->
                logger.trace("Validating session: {}", session)

                val user = userRepo.getUserBySession(session.token)
                if (user != null && user.id == session.userId) {
                    UserSession(user.id, session.token)
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect(this@configureSecurity.href(UserResource.Login()))
            }
        }
        session<UserSession>("admin-auth-session") {
            validate { session ->
                logger.trace("Validating admin session: {}", session)

                val user = userRepo.getUserBySession(session.token)
                if (user != null && user.id == session.userId && user.type == UserTypeEntity.ADMIN) {
                    UserSession(user.id, session.token)
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect(this@configureSecurity.href(UserResource.Login()))
            }
        }
    }
    install(Sessions) {
        cookie<UserSession>("auth_session") {
            cookie.path = "/"
            cookie.secure = !this@configureSecurity.developmentMode
            cookie.httpOnly = true
            cookie.extensions["SameSite"] = "Lax"
        }
    }
}
