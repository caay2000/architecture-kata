package com.github.caay2000.archkata.ex1

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

private lateinit var bussiness: Bussiness

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    start()
    configureRouting()
    configureSerialization()
}

fun Application.start() {
    bussiness = Bussiness()
}

fun Application.configureRouting() {
    routing {
        route("/user") {
            get("/{id}") {
                val id = call.parameters["id"]!!
                val result = bussiness.invoke("VIEW $id")
                call.respond(HttpStatusCode.OK, result)
            }
            get("/timeline/{id}") {
                val id = call.parameters["id"]!!
                val result = bussiness.invoke("TIMELINE $id")
                call.respond(HttpStatusCode.OK, result)
            }
            post("/{email}/{name}") {
                val email = call.parameters["email"]!!
                val name = call.parameters["name"]!!
                val result = bussiness.invoke("CREATE $email $name")
                call.respond(HttpStatusCode.Created, result)
            }
        }
        post("/write/{id}") {
            val id = call.parameters["id"]!!
            val message = call.receive<String>()
            val result = bussiness.invoke("WRITE $id $message")
            call.respond(HttpStatusCode.Created, result)
        }
        post("/follow/{id}/{followId}") {
            val id = call.parameters["id"]!!
            val followId = call.parameters["followId"]!!
            val result = bussiness.invoke("FOLLOW $id $followId")
            call.respond(HttpStatusCode.NoContent, result)
        }
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
