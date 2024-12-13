package com.jones.features.admin.view.html

import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title

internal data class AdminLandingViewModel(
    val title: String = "Admin Landing Page",
    val userContent: AdminUserContentViewModel,
)

internal fun HTML.adminLandingView(model: AdminLandingViewModel) {
    head {
        title(model.title)
    }

    body {
        h1 {
            + model.title
        }
        div {
            adminUserContentView(model.userContent)
        }
    }
}
