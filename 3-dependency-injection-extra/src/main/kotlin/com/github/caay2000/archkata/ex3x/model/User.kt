package com.github.caay2000.archkata.ex3x.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val messages: MutableList<Message> = mutableListOf(),
    val follows: MutableList<String> = mutableListOf()
)
