package com.github.caay2000.archkata.ex3x.database

import com.github.caay2000.archkata.ex3x.model.User
import com.github.caay2000.archkata.libraries.db.Datasource

class UserRepository(private val datasource: Datasource) {

    fun save(user: User) {
        datasource.save(TABLE_NAME, user.id, user)
    }

    fun get(id: String): User = datasource.getById<User>(TABLE_NAME, id)!!

    companion object {
        private const val TABLE_NAME = "user"
    }
}
