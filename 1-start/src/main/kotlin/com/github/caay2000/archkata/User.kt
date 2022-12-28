package com.github.caay2000.archkata

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val messages: MutableList<Message> = mutableListOf(),
    val follows: MutableList<Int> = mutableListOf()
)
