package com.jones.features.user.route

import io.ktor.resources.Resource

@Resource("/user")
class UserResource {

    @Resource("/all")
    class All(val parent: UserResource = UserResource())

    @Resource("/login")
    class Login(val parent: UserResource = UserResource())
}
