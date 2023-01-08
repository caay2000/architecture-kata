package com.github.caay2000.archkata.ex2.model

import java.time.LocalDateTime
import java.util.UUID

data class Message(
    val id: UUID,
    val user: String,
    val userId: UUID,
    val message: String,
    val date: LocalDateTime = LocalDateTime.now()
) {

    fun toJson() = """{
                "id": "$id",
                "user": "$user",
                "userId": "$userId",
                "message": "$message",
                "date": "$date"
            }"""
}
