package com.jones.integration.features.user

import com.jones.integration.usecase.CreateUserTestEnvUseCase
import com.jones.features.user.data.UserRepository
import com.jones.features.user.data.entity.UserTypeEntity
import com.jones.features.user.route.UserResource
import com.jones.features.user.route.model.CreateUserRequest
import com.jones.features.user.view.json.UserDto
import com.jones.features.user.view.json.UserTypeDto
import com.jones.withTestApplication
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.resources.href

class UserRestTest : BehaviorSpec({

    context("A user logging into an account and requesting their own data") {

        withTestApplication {
            val createUserUseCase = koin.get<CreateUserTestEnvUseCase>()

            afterTest { (testCase, _) ->
                if (testCase.name.prefix == "Given: ") {
                    cookieStorage.clear()
                }
            }

            Given("Logged in as an admin") {
                val loginRequest = client.submitForm(
                    url = application.href(UserResource.Login()),
                    formParameters = Parameters.build {
                        append("username", "admin")
                        append("password", "admin")
                    }
                )

                When("The admin requests their own data") {
                    val fetchUserResponse = client.get(UserResource())

                    Then("An ok response is received") {
                        fetchUserResponse.status shouldBe HttpStatusCode.OK
                    }

                    Then("The response contains the admin's data") {
                        val user = fetchUserResponse.body<UserDto>()
                        user.username shouldBe "admin"
                    }
                }
            }

            Given("A logged in user") {
                createUserUseCase.execute("user", "password".toCharArray())
                val loginRequest = client.submitForm(
                    url = application.href(UserResource.Login()),
                    formParameters = Parameters.build {
                        append("username", "user")
                        append("password", "password")
                    }
                )

                When("The user requests their own data") {
                    val fetchUserResponse = client.get(UserResource())

                    Then("An ok response is received") {
                        fetchUserResponse.status shouldBe HttpStatusCode.OK
                    }

                    Then("The response contains the user's data") {
                        val user = fetchUserResponse.body<UserDto>()
                        user.username shouldBe "user"
                    }
                }
            }

            Given("An unauthenticated user") {

                When("The user requests their own data") {
                    val fetchUserResponse = client.get(UserResource())

                    Then("A redirect response is received") {
                        fetchUserResponse.status shouldBe HttpStatusCode.Found
                        fetchUserResponse.headers["Location"] shouldBe application.href(UserResource.Login())
                    }
                }
            }
        }
    }

    context("An admin requesting all user data") {

        withTestApplication {
            val createUserUseCase = koin.get<CreateUserTestEnvUseCase>()

            afterTest { (testCase, _) ->
                if (testCase.name.prefix == "Given: ") {
                    cookieStorage.clear()
                }
            }

            Given("Logged in as an admin user AND there are users in the system") {
                createUserUseCase.execute("user1", "password".toCharArray())
                createUserUseCase.execute("user2", "password".toCharArray())
                val loginRequest = client.submitForm(
                    url = application.href(UserResource.Login()),
                    formParameters = Parameters.build {
                        append("username", "admin")
                        append("password", "admin")
                    }
                )

                When("The admin requests all user data") {
                    val fetchAllUsersResponse = client.get(UserResource.All())

                    Then("An ok response is received") {
                        fetchAllUsersResponse.status shouldBe HttpStatusCode.OK
                    }

                    Then("The response contains all users") {
                        val expectedUserNames = setOf("user1", "user2", "admin")
                        val actualUserNames = fetchAllUsersResponse.body<List<UserDto>>().map { it.username }.toSet()
                        actualUserNames shouldBe expectedUserNames
                    }
                }
            }

            Given("Logged in as a non-admin user") {
                val loginRequest = client.submitForm(
                    url = application.href(UserResource.Login()),
                    formParameters = Parameters.build {
                        append("username", "user1")
                        append("password", "password")
                    }
                )

                When("The user requests all user data") {
                    val fetchAllUsersResponse = client.get(UserResource.All())

                    Then("A redirect response is received") {
                        fetchAllUsersResponse.status shouldBe HttpStatusCode.Found
                        fetchAllUsersResponse.headers["Location"] shouldBe application.href(UserResource.Login())
                    }
                }
            }

            Given("An unauthenticated user") {

                When("The user requests all user data") {
                    val fetchAllUsersResponse = client.get(UserResource.All())

                    Then("A redirect response is received") {
                        fetchAllUsersResponse.status shouldBe HttpStatusCode.Found
                        fetchAllUsersResponse.headers["Location"] shouldBe application.href(UserResource.Login())
                    }
                }
            }
        }
    }

    context("An admin creating a new user") {

        withTestApplication {
            val userRepo = koin.get<UserRepository>()
            val createUserUseCase = koin.get<CreateUserTestEnvUseCase>()

            Given("Logged in as an admin user") {
                val loginRequest = client.submitForm(
                    url = application.href(UserResource.Login()),
                    formParameters = Parameters.build {
                        append("username", "admin")
                        append("password", "admin")
                    }
                )

                When("The admin creates a new user") {
                    val expectedUser = "the_new_user"
                    val createUserResponse = client.post(UserResource()) {
                        setBody(
                            CreateUserRequest(
                                username = expectedUser,
                                password = "password".toCharArray(),
                                type = UserTypeDto.USER,
                            )
                        )
                    }
                    val createdUser = userRepo.getUserByUsername(expectedUser)

                    Then("An ok response is received") {
                        createUserResponse.status shouldBe HttpStatusCode.OK
                    }

                    Then("A new user is created with the correct name") {
                        createdUser?.username shouldBe expectedUser
                    }

                    Then("A new user is created with the matching type") {
                        createdUser?.type shouldBe UserTypeEntity.USER
                    }
                }
            }

            Given("A user who is not an admin") {
                createUserUseCase.execute("the_non_admin_user", "password".toCharArray())
                val loginRequest = client.submitForm(
                    url = application.href(UserResource.Login()),
                    formParameters = Parameters.build {
                        append("username", "the_non_admin_user")
                        append("password", "password")
                    }
                )

                When("The user tries to create a new user when authenticated") {
                    val expectedUser = "the_illegal_user"
                    val createUserResponse = client.post(UserResource()) {
                        setBody(
                            CreateUserRequest(
                                username = expectedUser,
                                password = "password".toCharArray(),
                                type = UserTypeDto.USER,
                            )
                        )
                    }

                    Then("A redirect to login response is received") {
                        createUserResponse.status shouldBe HttpStatusCode.Found
                        createUserResponse.headers["Location"] shouldBe application.href(UserResource.Login())
                    }

                    Then("No new user is created") {
                        val createdUser = userRepo.getUserByUsername(expectedUser)
                        createdUser shouldBe null
                    }
                }

                When("The user tries to create a new user when not authenticated") {
                    val expectedUser = "the_unauthenticated_user"
                    val createUserResponse = client.post(UserResource()) {
                        setBody(
                            CreateUserRequest(
                                username = expectedUser,
                                password = "password".toCharArray(),
                                type = UserTypeDto.USER,
                            )
                        )
                    }

                    Then("A redirect to login response is received") {
                        createUserResponse.status shouldBe HttpStatusCode.Found
                        createUserResponse.headers["Location"] shouldBe application.href(UserResource.Login())
                    }

                    Then("No new user is created") {
                        val createdUser = userRepo.getUserByUsername(expectedUser)
                        createdUser shouldBe null
                    }
                }
            }
        }
    }
})
