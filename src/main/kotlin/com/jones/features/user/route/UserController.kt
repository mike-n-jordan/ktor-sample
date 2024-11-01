package com.jones.features.user.route

import com.jones.features.user.domain.UserService
import com.jones.features.user.route.model.CreateUserRequest
import com.jones.features.user.view.html.LoginFormViewModel
import com.jones.features.user.view.html.userLoginFormView
import com.jones.features.user.view.json.toDto
import com.jones.features.user.view.json.toModel
import com.jones.plugins.UserSession
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal
import io.ktor.server.html.respondHtml
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.resources.href
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

internal class UserController(
    private val userService: UserService,
) {

    suspend fun createNewUser(call: ApplicationCall) {
        val createUserRequest = call.receive<CreateUserRequest>()

        val user = userService.createUser(
            createUserRequest.username,
            createUserRequest.password,
            createUserRequest.type.toModel()
        )

        call.respond(user.toDto())
    }

    suspend fun serveLoginPage(call: ApplicationCall) {
        call.respondLoginFormView()
    }

    suspend fun fetchAllUserData(call: ApplicationCall) {
        val users = userService.fetchAllUsers().map { user -> user.toDto() }
        call.respond(users)
    }

    suspend fun performLogin(call: ApplicationCall) {
        val params = call.receiveParameters()
        val username = params["username"]
            ?: return call.respondLoginFormView("Username not provided")
        val password = params["password"]
            ?.toCharArray()
            ?: return call.respondLoginFormView("Password not provided")

        val session = userService.createSessionFor(username, password)
            ?: return call.respondLoginFormView("Incorrect password or username")

        call.sessions.set(UserSession(session.userId, session.token))
        call.respondRedirect("/") // TODO redirect properly
    }

    suspend fun fetchUserData(call: ApplicationCall) {
        val userId = requireNotNull(call.principal<UserSession>()?.userId) {
            "No valid session found"
        }
        val user = requireNotNull(userService.fetchUser(userId)) {
            "User not found"
        }
        call.respond(user.toDto())
    }

    private suspend fun ApplicationCall.respondLoginFormView(error: String? = null) {
        val model = LoginFormViewModel(
            formAction = application.href(UserResource.Login()),
            errorMessage = error,
        )
        val code = if (error != null) HttpStatusCode.BadRequest else HttpStatusCode.OK
        respondHtml(code) { userLoginFormView(model) }
    }
}
