package com.github.caay2000.archkata.ex4.domain

data class User(
    val id: String,
    val email: String,
    val name: String,
    val messages: MutableList<Message> = mutableListOf(),
    val follows: MutableList<String> = mutableListOf()
)
