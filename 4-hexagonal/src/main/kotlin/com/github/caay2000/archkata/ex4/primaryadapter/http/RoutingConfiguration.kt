package com.github.caay2000.archkata.ex4.primaryadapter.http

import com.github.caay2000.archkata.ex4.application.SocialNetworkService
import com.github.caay2000.archkata.ex4.infra.ApplicationContext
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    val service = ApplicationContext.getBean<SocialNetworkService>("socialNetworkService")
    routing {
        viewUser(service)
        viewTimeline(service)
        createUser(service)
        writeMessage(service)
        followUser(service)
    }
}
