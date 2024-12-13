package com.jones.features.admin.view.html

import com.jones.features.admin.domain.model.UserModel
import com.jones.features.admin.domain.model.UserType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.TagConsumer
import kotlinx.html.div

internal data class AdminUserContentViewModel(
    val users: List<UserViewModel>,
)

internal data class UserViewModel(
    val id: String,
    val username: String,
    val type: UserTypeViewModel,
)

internal enum class UserTypeViewModel {
    ADMIN,
    USER
}

internal fun UserModel.toViewModel() =
    UserViewModel(
        id = id,
        username = username,
        type = type.toViewModel()
    )

internal fun UserType.toViewModel() =
    when (this) {
        UserType.ADMIN -> UserTypeViewModel.ADMIN
        UserType.USER -> UserTypeViewModel.USER
    }

internal fun TagConsumer<String>.adminUserContentView(model: AdminUserContentViewModel): String =
    div {
        adminUserContentView(model)
    }

internal fun FlowContent.adminUserContentView(model: AdminUserContentViewModel) {
   div {
        adminUserContentViewBody(model)
   }
}

private fun DIV.adminUserContentViewBody(model: AdminUserContentViewModel) {
    div {
        + "User Content"
    }
}
