package com.jones.plugins

import com.jones.features.admin.route.adminRoute
import com.jones.features.user.route.userRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.webjars.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Routing")

internal fun Application.configureRouting() {
    install(Resources)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is IllegalArgumentException -> call.respond(HttpStatusCode.BadRequest)
                else -> {
                    logger.error("Full stack trace", cause)
                    call.respondText(text = "Unhandled: $cause", status = HttpStatusCode.InternalServerError)
                }
            }
        }
    }
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }
    routing {
        userRoute()
        adminRoute()
    }
}
