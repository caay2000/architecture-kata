package com.github.caay2000.archkata.ex4.primaryadapter.http

import com.github.caay2000.archkata.ex4.application.SocialNetworkService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.followUser(service: SocialNetworkService) {
    post("/follow/{id}/{followId}") {
        val id = call.parameters["id"]!!
        val followId = call.parameters["followId"]!!
        val result = service.follow(id, followId)
        call.respond(HttpStatusCode.NoContent, result)
    }
}
