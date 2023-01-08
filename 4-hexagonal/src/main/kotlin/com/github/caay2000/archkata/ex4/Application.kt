package com.github.caay2000.archkata.ex4

import com.github.caay2000.archkata.ex4.application.SocialNetworkService
import com.github.caay2000.archkata.ex4.database.InMemoryUserRepository
import com.github.caay2000.archkata.ex4.infra.ApplicationContext.getBean
import com.github.caay2000.archkata.ex4.infra.ApplicationContext.registerBean
import com.github.caay2000.archkata.ex4.primaryadapter.http.configureRouting
import com.github.caay2000.archkata.ex4.secondaryadapter.database.Datasource
import com.github.caay2000.archkata.ex4.secondaryadapter.database.InMemoryDatabase
import com.github.caay2000.archkata.ex4.secondaryadapter.date.LocalDateProvider
import com.github.caay2000.archkata.ex4.secondaryadapter.uuid.UUIDIdGenerator
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    dependencyInjection()
    configureRouting()
    configureSerialization()
}

fun Application.cleanDatabase() {
    val datasource = getBean<Datasource>("datasource")
    datasource.clean()
}

fun Application.dependencyInjection() {
    registerBean("datasource", InMemoryDatabase())
    registerBean("userRepository", InMemoryUserRepository(getBean("datasource")))
    registerBean("idGenerator", UUIDIdGenerator())
    registerBean("dateProvider", LocalDateProvider())

    registerBean(
        name = "socialNetworkService",
        bean = SocialNetworkService(
            getBean("userRepository"),
            getBean("idGenerator"),
            getBean("dateProvider")
        )
    )
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
