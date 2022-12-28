package com.github.caay2000.archkata

import java.time.LocalDateTime

data class Message(
    val id: Int,
    val user: String,
    val userId: Int,
    val message: String,
    val date: LocalDateTime = LocalDateTime.now()
) {

    fun toJson() = """{
                "id": $id,
                "user": "$user",
                "userId": $userId,
                "message": "$message",
                "date": "$date"
            }"""
}
