package com.github.caay2000.archkata.ex4.primaryadapter.http

import com.github.caay2000.archkata.ex4.application.SocialNetworkServiceApi
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.createUser(service: SocialNetworkServiceApi) {
    post("user/{email}/{name}") {
        val email = call.parameters["email"]!!
        val name = call.parameters["name"]!!
        val result = service.createUser(email, name)
        call.respond(HttpStatusCode.Created, result)
    }
}
