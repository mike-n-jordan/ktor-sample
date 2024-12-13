package com.jones.features.admin.route

import io.ktor.server.auth.authenticate
import io.ktor.server.resources.get
import io.ktor.server.routing.Routing
import io.ktor.server.routing.application
import org.koin.ktor.ext.getKoin

fun Routing.adminRoute() {
    val controller = application.getKoin().get<AdminController>()

    authenticate("admin-auth-session") {
        /*
            Get admin page
         */
        get<AdminResource> {
            controller.getAdminPage(call)
        }

        /*
            Get users list
         */
        get<AdminResource.Users> {
            controller.getUsersList(call)
        }
    }
}
