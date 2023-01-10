package com.github.caay2000.archkata.ex2

import com.github.caay2000.archkata.ex2.service.SocialNetworkService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.util.UUID

private lateinit var socialNetworkService: SocialNetworkService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    start()
    configureRouting()
}

fun Application.start() {
    socialNetworkService = SocialNetworkService()
}

fun Application.configureRouting() {
    routing {
        route("/user") {
            get("/{id}") {
                val id = call.parameters["id"]!!
                val result = socialNetworkService.view(UUID.fromString(id))
                call.respond(HttpStatusCode.OK, result)
            }
            get("/timeline/{id}") {
                val id = call.parameters["id"]!!
                val result = socialNetworkService.timeline(UUID.fromString(id))
                call.respond(HttpStatusCode.OK, result)
            }
            post("/{email}/{name}") {
                val email = call.parameters["email"]!!
                val name = call.parameters["name"]!!
                val result = socialNetworkService.createUser(email, name)
                call.respond(HttpStatusCode.Created, result)
            }
        }
        post("/write/{id}") {
            val id = call.parameters["id"]!!
            val message = call.receive<String>()
            val result = socialNetworkService.write(UUID.fromString(id), message)
            call.respond(HttpStatusCode.Created, result)
        }
        post("/follow/{id}/{followId}") {
            val id = call.parameters["id"]!!
            val followId = call.parameters["followId"]!!
            val result = socialNetworkService.follow(UUID.fromString(id), UUID.fromString(followId))
            call.respond(HttpStatusCode.NoContent, result)
        }
    }
}
