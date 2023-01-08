package com.github.caay2000.archkata.ex4.secondaryadapter.database

import com.github.caay2000.archkata.ex4.application.UserRepository
import com.github.caay2000.archkata.ex4.domain.User
import com.github.caay2000.archkata.ex4.infra.Datasource

class InMemoryUserRepository(private val datasource: Datasource) : UserRepository {

    override fun save(user: User) {
        datasource.save(TABLE_NAME, user.id, user)
    }

    override fun get(id: String): User = datasource.getById<User>(TABLE_NAME, id)!!

    companion object {
        private const val TABLE_NAME = "user"
    }
}
