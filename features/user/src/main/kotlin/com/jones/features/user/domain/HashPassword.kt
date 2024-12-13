package com.jones.features.user.domain

import de.mkammerer.argon2.Argon2Factory

private const val ITERATIONS = 10
private const val MEMORY = 65536
private const val PARALLELISM = 1

fun CharArray.hashPassword(): String {
    val argon2 = Argon2Factory.create()
    val hashedPassword = argon2.hash(ITERATIONS, MEMORY, PARALLELISM, this)
    argon2.wipeArray(this)
    return hashedPassword
}
