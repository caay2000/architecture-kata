package com.github.caay2000.archkata.ex3x.model

import java.time.LocalDateTime

data class Message(
    val id: String,
    val user: String,
    val userId: String,
    val message: String,
    val date: LocalDateTime
) {

    fun toJson() = """{
                "id": "$id",
                "user": "$user",
                "userId": "$userId",
                "message": "$message",
                "date": "$date"
            }"""
}
