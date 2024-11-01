package com.jones

import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url

class ClearableCookieStorage : CookiesStorage  {

    private var acceptCookieStorage = AcceptAllCookiesStorage()

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        acceptCookieStorage.addCookie(requestUrl, cookie)
    }

    override fun close() {
        acceptCookieStorage.close()
    }

    override suspend fun get(requestUrl: Url): List<Cookie> =
        acceptCookieStorage.get(requestUrl)

    fun clear() {
        acceptCookieStorage.close()
        acceptCookieStorage = AcceptAllCookiesStorage()
    }
}
