package com.github.caay2000.archkata.ex4.application

import com.github.caay2000.archkata.ex4.domain.Message
import com.github.caay2000.archkata.ex4.domain.User

class SocialNetworkService(
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator,
    private val dateProvider: DateProvider
) {

    fun createUser(email: String, name: String): String {
        val user = User(idGenerator.generate(), email, name)
        userRepository.save(user)
        return """
            {
            	"id": "${user.id}",
            	"email": "${user.email}",
            	"name": "${user.name}"
            }
        """.trimIndent()
    }

    fun write(userId: String, message: String): String {
        val user = userRepository.get(userId)
        val msg = Message(idGenerator.generate(), user.name, userId, message, dateProvider.now())
        user.messages.add(msg)
        userRepository.save(user)
        return """ ${msg.toJson()} """
    }

    fun timeline(userId: String): String {
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

    fun view(userId: String): String {
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

    fun follow(userId: String, followUserId: String): String {
        val user = userRepository.get(userId)
        val userFollowed = userRepository.get(followUserId)
        user.follows.add(userFollowed.id)
        userRepository.save(user)
        return ""
    }

    private fun userFollowJson(id: String): String {
        val user = userRepository.get(id)
        return """{"id": "${user.id}", "name":"${user.name}" }"""
    }
}
