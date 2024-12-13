package com.jones.features.admin.route

import com.jones.features.admin.domain.AdminService
import com.jones.features.admin.view.html.AdminLandingViewModel
import com.jones.features.admin.view.html.AdminUserContentViewModel
import com.jones.features.admin.view.html.adminLandingView
import com.jones.features.admin.view.html.adminUserContentView
import com.jones.features.admin.view.html.toViewModel
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respondText
import kotlinx.html.stream.createHTML

internal class AdminController(private val service: AdminService) {

    suspend fun getAdminPage(call: ApplicationCall) {
        val users = service.getAllUsers(call.request.cookies.rawCookies)
            .map { user -> user.toViewModel() }
        val model = AdminLandingViewModel(userContent = AdminUserContentViewModel(users = users))

        call.respondHtml { adminLandingView(model) }
    }

    suspend fun getUsersList(call: ApplicationCall) {
        val users = service.getAllUsers(call.request.cookies.rawCookies)
            .map { user -> user.toViewModel() }
        val model = AdminUserContentViewModel(users)

        call.respondText(ContentType.Text.Html) { createHTML().adminUserContentView(model) }
    }
}
