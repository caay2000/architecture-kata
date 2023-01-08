package com.github.caay2000.archkata.ex1

class Bussiness {

    private var userIndex = 1
    private var messageIndex = 1
    private val users = mutableListOf<User>()

    fun invoke(input: String): String {
        val words = input.split(" ")
        return if (words[0] == "CREATE") {
            createUser(words[1], words[2])
        } else if (words[0] == "WRITE") {
            write(words[1].toInt(), words.takeLast(words.size - 2).joinToString(" "))
        } else if (words[0] == "TIMELINE") {
            timeline(words[1].toInt())
        } else if (words[0] == "VIEW") {
            view(words[1].toInt())
        } else if (words[0] == "FOLLOW") {
            follow(words[1].toInt(), words[2].toInt())
        } else {
            "ERROR"
        }
    }

    private fun createUser(email: String, name: String): String {
        val user = User(userIndex, email, name)
        users.add(user)
        userIndex += 1
        return """
            {
            	"id": ${user.id},
            	"email": "${user.email}",
            	"name": "${user.name}"
            }
        """.trimIndent()
    }

    private fun write(userId: Int, message: String): String {
        val user = findUser(userId)
        val msg = Message(messageIndex, user.name, userId, message)
        user.messages.add(msg)
        messageIndex += 1
        return """ ${msg.toJson()} """
    }

    private fun timeline(userId: Int): String {
        val user = findUser(userId)
        val messages = (user.messages + user.follows.flatMap { findUser(it).messages }).sortedByDescending { it.date }
        return """
            {
                "id": ${user.id},
                "email": "${user.email}",
                "name": "${user.name}",
                "messages": [ ${messages.joinToString(",") { it.toJson() }} ]
            }
        """.trimIndent()
    }

    private fun view(userId: Int): String {
        val user = findUser(userId)
        return """
            {
                "id": ${user.id},
                "email": "${user.email}",
                "name": "${user.name}",
                "messages": [ ${user.messages.joinToString(",") { it.toJson() }} ],
                "follows": [ ${user.follows.joinToString(",") { userFollowJson(it) }} ]
            }
        """.trimIndent()
    }

    private fun userFollowJson(id: Int): String {
        val user = findUser(id)
        return """{"id": "${user.id}", "name":"${user.name}" }"""
    }

    private fun follow(userId: Int, followUserId: Int): String {
        val user = findUser(userId)
        val userFollowed = findUser(followUserId)
        user.follows.add(userFollowed.id)
        return ""
    }

    private fun findUser(userId: Int) = users.find { user -> user.id == userId }!!
}
