package com.github.caay2000.archkata.ex1

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val messages: MutableList<Message> = mutableListOf(),
    val follows: MutableList<Int> = mutableListOf()
)
