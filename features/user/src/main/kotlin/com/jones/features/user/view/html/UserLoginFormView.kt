package com.jones.features.user.view.html

import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.p

internal data class LoginFormViewModel(
    val formAction: String,
    val errorMessage: String? = null,
)

internal fun HTML.userLoginFormView(model: LoginFormViewModel) {
    body {
        form {
            method = FormMethod.post
            action = model.formAction

            h2 {
                + "Login"
            }

            if (model.errorMessage != null) {
                p {
                    id = "login-error"
                    + model.errorMessage
                }
            }

            div {
                label {
                    attributes["for"] = "username"
                    + "Username"
                }
                input {
                    type = InputType.text
                    name = "username"
                    placeholder = "Username"
                }
            }

            div {
                label {
                    attributes["for"] = "password"
                    + "Password"
                }
                input {
                    type = InputType.password
                    name = "password"
                    placeholder = "Password"
                }
            }

            div {
                input {
                    type = InputType.submit
                    value = "Login"
                    name = "submit"
                }
            }
        }
    }
}
