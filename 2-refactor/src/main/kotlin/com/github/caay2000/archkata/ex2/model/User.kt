package com.github.caay2000.archkata.ex2.model

import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val name: String,
    val messages: MutableList<Message> = mutableListOf(),
    val follows: MutableList<UUID> = mutableListOf()
)
