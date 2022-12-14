package com.github.caay2000.archkata.ex3

import com.github.caay2000.archkata.ex3.infra.LocalDateProvider
import com.github.caay2000.archkata.ex3.infra.UUIDGenerator
import com.github.caay2000.archkata.ex3.repository.UserRepository
import com.github.caay2000.archkata.ex3.service.SocialNetworkService
import com.github.caay2000.archkata.libraries.db.InMemoryDatasource
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

private lateinit var service: SocialNetworkService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    start()
    configureRouting()
}

fun start() {
    val datasource = InMemoryDatasource()
    val idGenerator = UUIDGenerator()
    val dateProvider = LocalDateProvider()
    val userRepository = UserRepository(datasource)

    service = SocialNetworkService(userRepository, idGenerator, dateProvider)
}

fun Application.configureRouting() {
    routing {
        route("/user") {
            get("/{id}") {
                val id = call.parameters["id"]!!
                val result = service.view(id)
                call.respond(HttpStatusCode.OK, result)
            }
            get("/timeline/{id}") {
                val id = call.parameters["id"]!!
                val result = service.timeline(id)
                call.respond(HttpStatusCode.OK, result)
            }
            post("/{email}/{name}") {
                val email = call.parameters["email"]!!
                val name = call.parameters["name"]!!
                val result = service.createUser(email, name)
                call.respond(HttpStatusCode.Created, result)
            }
        }
        post("/write/{id}") {
            val id = call.parameters["id"]!!
            val message = call.receive<String>()
            val result = service.write(id, message)
            call.respond(HttpStatusCode.Created, result)
        }
        post("/follow/{id}/{followId}") {
            val id = call.parameters["id"]!!
            val followId = call.parameters["followId"]!!
            val result = service.follow(id, followId)
            call.respond(HttpStatusCode.NoContent, result)
        }
    }
}
