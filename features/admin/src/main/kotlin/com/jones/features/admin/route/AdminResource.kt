package com.jones.features.admin.route

import io.ktor.resources.Resource

@Resource("/admin")
class AdminResource {

    @Resource("/users")
    class Users(val parent: AdminResource = AdminResource())
}
