package com.github.caay2000.archkata.ex3x

import com.github.caay2000.archkata.ex3x.database.UserRepository
import com.github.caay2000.archkata.ex3x.infra.LocalDateProvider
import com.github.caay2000.archkata.ex3x.infra.UUIDGenerator
import com.github.caay2000.archkata.ex3x.infra.di.ApplicationContext
import com.github.caay2000.archkata.ex3x.service.SocialNetworkService
import com.github.caay2000.archkata.libraries.db.InMemoryDatasource
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

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    start()
    configureRouting()
    configureSerialization()
}

fun Application.start() {
    ApplicationContext.registerBean(InMemoryDatasource::class)
    ApplicationContext.registerBean(UUIDGenerator::class)
    ApplicationContext.registerBean(LocalDateProvider::class)
    ApplicationContext.registerBean(UserRepository::class)
    ApplicationContext.registerBean(SocialNetworkService::class)
}

fun Application.configureRouting() {
    val service: SocialNetworkService = ApplicationContext.getBean(SocialNetworkService::class)
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

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
