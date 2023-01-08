package com.github.caay2000.archkata.ex2.database

import com.github.caay2000.archkata.ex2.model.User
import java.util.UUID

class UserRepository {

    private val database = InMemoryDatabase()

    fun save(user: User) {
        database.save(TABLE_NAME, user.id.toString(), user)
    }

    fun get(id: UUID): User = database.getById<User>(TABLE_NAME, id.toString())!!

    companion object {
        private const val TABLE_NAME = "user"
    }
}
