package com.github.caay2000.archkata.ex4.application

import com.github.caay2000.archkata.ex4.domain.Follow
import com.github.caay2000.archkata.ex4.domain.Message
import com.github.caay2000.archkata.ex4.domain.User

class SocialNetworkService(
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator,
    private val dateProvider: DateProvider
) : SocialNetworkServiceApi {

    override fun createUser(email: String, name: String): UserResponse {
        val user = User(idGenerator.generate(), email, name)
        userRepository.save(user)
        return user.toUserResponse()
    }

    override fun viewUser(userId: String): UserResponse = userRepository.get(userId).toUserResponse()

    override fun write(userId: String, message: String): MessageResponse {
        val user = userRepository.get(userId)
        val msg = Message(idGenerator.generate(), user.name, userId, message, dateProvider.now())
        user.messages.add(msg)
        userRepository.save(user)
        return msg.toMessageResponse()
    }

    override fun timeline(userId: String): TimelineResponse {
        val user = userRepository.get(userId)
        val timelineMessages = (user.messages + user.follows.flatMap { userRepository.get(it.id).messages })
            .sortedByDescending { it.date }
        val timelineUser = user.copy(messages = timelineMessages.toMutableList())
        return timelineUser.toTimelineResponse()
    }

    override fun follow(userId: String, followUserId: String) {
        val user = userRepository.get(userId)
        val userFollowed = userRepository.get(followUserId)
        user.follows.add(Follow(userFollowed.id, userFollowed.name))
        userRepository.save(user)
    }
}
