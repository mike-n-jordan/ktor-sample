package com.jones.integration.features.admin

import com.jones.withLiveApplication
import io.kotest.core.spec.style.BehaviorSpec
import org.openqa.selenium.chrome.ChromeOptions

class AdminUiTest : BehaviorSpec({

    val options = ChromeOptions()
        .apply { addArguments("--headless") }

    context("Loading the admin landing page") {

        withLiveApplication {

            Given("A valid admin user") {

            }
        }
    }
})
