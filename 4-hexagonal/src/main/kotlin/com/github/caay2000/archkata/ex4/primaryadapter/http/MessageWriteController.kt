package com.github.caay2000.archkata.ex4.primaryadapter.http

import com.github.caay2000.archkata.ex4.application.SocialNetworkServiceApi
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.writeMessage(service: SocialNetworkServiceApi) {
    post("/write/{id}") {
        val id = call.parameters["id"]!!
        val message = call.receive<String>()
        val result = service.write(id, message)
        call.respond(HttpStatusCode.Created, result)
    }
}
