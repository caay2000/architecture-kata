package com.github.caay2000.archkata.ex4.application

import com.github.caay2000.archkata.ex4.domain.User

interface UserRepository {

    fun save(user: User)
    fun get(id: String): User
}
