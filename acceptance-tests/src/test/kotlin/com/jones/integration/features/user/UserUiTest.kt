package com.jones.integration.features.user

import com.jones.integration.usecase.CreateUserTestEnvUseCase
import com.jones.features.user.route.UserResource
import com.jones.withLiveApplication
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeOptions

class UserUiTest : BehaviorSpec({

    val options = ChromeOptions()
        .apply { addArguments("--headless") }

    context("User logs in via a web browser") {

        withLiveApplication {
            val createUserUseCase = koin.get<CreateUserTestEnvUseCase>()

            Given("The user is not logged in") {
                options.useDriver { driver ->

                    When("The user requests the login page") {
                        driver.get(href(UserResource.Login()))

                        Then("The user is shown the login form") {
                            driver.findElement(By.tagName("form")).shouldNotBeNull()
                        }

                        Then("The login form contains a username field") {
                            driver.findElement(By.name("username")).shouldNotBeNull()
                        }

                        Then("The login form contains a password field") {
                            driver.findElement(By.name("password")).shouldNotBeNull()
                        }

                        Then("The login form contains a submit button") {
                            driver.findElement(By.name("submit")).shouldNotBeNull()
                        }
                    }
                }
            }

            Given("The user requests the login page") {
                createUserUseCase.execute("testuser", "testpassword".toCharArray())
                options.useDriver { driver ->
                    driver.get(href(UserResource.Login()))

                    When("The user submits their invalid credentials") {
                        driver.findElement(By.name("username")).sendKeys("testuser")
                        driver.findElement(By.name("password")).sendKeys("wrongpassword")
                        driver.findElement(By.name("submit")).click()
                        delay(500)

                        Then("THe user remains on the login page") {
                            driver.currentUrl shouldBe href(UserResource.Login())
                        }

                        Then("The user is shown an error message") {
                            driver.findElement(By.id("login-error")).text?.shouldNotBeNull()
                        }
                    }

                    When("The user submits their valid credentials for a valid user") {
                        driver.findElement(By.name("username")).sendKeys("testuser")
                        driver.findElement(By.name("password")).sendKeys("testpassword")
                        driver.findElement(By.name("submit")).click()
                        delay(500)

                        Then("The user is redirected to the home page") {
                            driver.currentUrl shouldBe "http://localhost:8080/"
                        }
                    }
                }
            }
        }
    }
})
