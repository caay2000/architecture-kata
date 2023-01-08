package com.github.caay2000.archkata.ex2.service

import com.github.caay2000.archkata.ex2.database.UserRepository
import com.github.caay2000.archkata.ex2.model.Message
import com.github.caay2000.archkata.ex2.model.User
import java.util.UUID

class SocialNetworkService {

    private val userRepository: UserRepository = UserRepository()

    fun createUser(email: String, name: String): String {
        val user = User(UUID.randomUUID(), email, name)
        userRepository.save(user)
        return """
            {
            	"id": "${user.id}",
            	"email": "${user.email}",
            	"name": "${user.name}"
            }
        """.trimIndent()
    }

    fun write(userId: UUID, message: String): String {
        val user = userRepository.get(userId)
        val msg = Message(UUID.randomUUID(), user.name, userId, message)
        user.messages.add(msg)
        userRepository.save(user)
        return """ ${msg.toJson()} """
    }

    fun timeline(userId: UUID): String {
        val user = userRepository.get(userId)
        val messages = (user.messages + user.follows.flatMap { userRepository.get(it).messages }).sortedByDescending { it.date }
        return """
            {
                "id": "${user.id}",
                "email": "${user.email}",
                "name": "${user.name}",
                "messages": [ ${messages.joinToString(",") { it.toJson() }} ]
            }
        """.trimIndent()
    }

    fun view(userId: UUID): String {
        val user = userRepository.get(userId)
        return """
            {
                "id": "${user.id}",
                "email": "${user.email}",
                "name": "${user.name}",
                "messages": [ ${user.messages.joinToString(",") { it.toJson() }} ],
                "follows": [ ${user.follows.joinToString(",") { userFollowJson(it) }} ]
            }
        """.trimIndent()
    }

    fun follow(userId: UUID, followUserId: UUID): String {
        val user = userRepository.get(userId)
        val userFollowed = userRepository.get(followUserId)
        user.follows.add(userFollowed.id)
        userRepository.save(user)
        return ""
    }

    private fun userFollowJson(id: UUID): String {
        val user = userRepository.get(id)
        return """{"id": "${user.id}", "name":"${user.name}" }"""
    }
}
