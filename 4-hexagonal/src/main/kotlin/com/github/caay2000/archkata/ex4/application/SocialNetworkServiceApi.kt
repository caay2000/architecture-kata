package com.github.caay2000.archkata.ex4.application

import com.github.caay2000.archkata.ex4.domain.Follow
import com.github.caay2000.archkata.ex4.domain.Message
import com.github.caay2000.archkata.ex4.domain.User
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

interface SocialNetworkServiceApi {

    fun createUser(email: String, name: String): UserResponse
    fun viewUser(userId: String): UserResponse

    fun write(userId: String, message: String): MessageResponse

    fun follow(userId: String, followUserId: String)

    fun timeline(userId: String): TimelineResponse
}

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val messages: Set<MessageResponse>,
    val follows: Set<SimpleUserResponse>
)

internal fun User.toUserResponse() = UserResponse(
    id = id,
    email = email,
    name = name,
    messages = messages.map { it.toMessageResponse() }.toSet(),
    follows = follows.map { it.toSimpleUserResponse() }.toSet()
)

@Serializable
data class SimpleUserResponse(
    val id: String,
    val name: String
)

internal fun Follow.toSimpleUserResponse() = SimpleUserResponse(id = id, name = name)

@Serializable
data class MessageResponse(
    val id: String,
    val user: String,
    val userId: String,
    val message: String,
    val date: LocalDateTime
)

internal fun Message.toMessageResponse() = MessageResponse(
    id = id,
    user = user,
    userId = userId,
    message = message,
    date = date
)

@Serializable
data class TimelineResponse(
    val id: String,
    val email: String,
    val name: String,
    val messages: Set<MessageResponse>
)

internal fun User.toTimelineResponse() =
    TimelineResponse(
        id = id,
        email = email,
        name = name,
        messages = messages.map { it.toMessageResponse() }.toSet()
    )
