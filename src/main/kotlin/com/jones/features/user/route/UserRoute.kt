package com.jones.features.user.route

import io.ktor.server.auth.authenticate
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.routing.Routing
import io.ktor.server.routing.application
import org.koin.ktor.ext.getKoin

fun Routing.userRoute() {
    val controller = application.getKoin().get<UserController>()

    authenticate("auth-session") {
        /*
            Get logged in user data
         */
        get<UserResource> {
            controller.fetchUserData(call)
        }
    }

    authenticate("admin-auth-session") {
        /*
            Create new user
         */
        post<UserResource> {
            controller.createNewUser(call)
        }

        /*
            Get all users
         */
        get<UserResource.All> {
            controller.fetchAllUserData(call)
        }
    }

    /*
        Get login form
     */
    get<UserResource.Login> {
        controller.serveLoginPage(call)
    }

    /*
        Submit login form
     */
    post<UserResource.Login> {
        controller.performLogin(call)
    }
}
