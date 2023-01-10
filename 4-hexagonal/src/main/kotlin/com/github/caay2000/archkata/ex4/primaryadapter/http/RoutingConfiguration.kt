package com.github.caay2000.archkata.ex4.primaryadapter.http

import com.github.caay2000.archkata.ex4.application.SocialNetworkServiceApi
import com.github.caay2000.archkata.libraries.di.ApplicationContext
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    val service: SocialNetworkServiceApi = ApplicationContext.getBean(SocialNetworkServiceApi::class)
    routing {
        viewUser(service)
        viewTimeline(service)
        createUser(service)
        writeMessage(service)
        followUser(service)
    }
}
