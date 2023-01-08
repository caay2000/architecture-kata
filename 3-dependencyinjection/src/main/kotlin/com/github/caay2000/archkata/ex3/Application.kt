package com.github.caay2000.archkata.ex3

import com.github.caay2000.archkata.ex3.infra.Datasource
import com.github.caay2000.archkata.ex3.infra.DateProvider
import com.github.caay2000.archkata.ex3.infra.IdGenerator
import com.github.caay2000.archkata.ex3.infra.InMemoryDatabase
import com.github.caay2000.archkata.ex3.infra.LocalDateProvider
import com.github.caay2000.archkata.ex3.infra.UUIDIdGenerator
import com.github.caay2000.archkata.ex3.service.SocialNetworkService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

private lateinit var socialNetworkService: SocialNetworkService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    start()
    configureRouting()
    configureSerialization()
}

fun Application.start() {

    val datasource: Datasource = InMemoryDatabase()
    val idGenerator: IdGenerator = UUIDIdGenerator()
    val dateProvider: DateProvider = LocalDateProvider()

    socialNetworkService = SocialNetworkService(datasource, idGenerator, dateProvider)
}

fun Application.configureRouting() {
    routing {
        route("/user") {
            get("/{id}") {
                val id = call.parameters["id"]!!
                val result = socialNetworkService.view(id)
                call.respond(HttpStatusCode.OK, result)
            }
            get("/timeline/{id}") {
                val id = call.parameters["id"]!!
                val result = socialNetworkService.timeline(id)
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
            val result = socialNetworkService.write(id, message)
            call.respond(HttpStatusCode.Created, result)
        }
        post("/follow/{id}/{followId}") {
            val id = call.parameters["id"]!!
            val followId = call.parameters["followId"]!!
            val result = socialNetworkService.follow(id, followId)
            call.respond(HttpStatusCode.NoContent, result)
        }
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
