package com.github.caay2000.archkata.ex4.primaryadapter.http

import com.github.caay2000.archkata.ex4.application.SocialNetworkService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.viewUser(service: SocialNetworkService) {
    get("user/{id}") {
        val id = call.parameters["id"]!!
        val result = service.view(id)
        call.respond(HttpStatusCode.OK, result)
    }
}
