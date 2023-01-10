package com.github.caay2000.archkata.ex4

import com.github.caay2000.archkata.ex4.application.SocialNetworkService
import com.github.caay2000.archkata.ex4.primaryadapter.http.configureRouting
import com.github.caay2000.archkata.ex4.secondaryadapter.database.InMemoryUserRepository
import com.github.caay2000.archkata.ex4.secondaryadapter.date.LocalDateProvider
import com.github.caay2000.archkata.ex4.secondaryadapter.uuid.UUIDGenerator
import com.github.caay2000.archkata.libraries.db.Datasource
import com.github.caay2000.archkata.libraries.db.InMemoryDatasource
import com.github.caay2000.archkata.libraries.di.ApplicationContext.getBean
import com.github.caay2000.archkata.libraries.di.ApplicationContext.init
import com.github.caay2000.archkata.libraries.di.ApplicationContext.registerBean
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

fun cleanDatabase() {
    val datasource: Datasource = getBean(Datasource::class)
    datasource.clean()
}

fun dependencyInjection() {
    registerBean(InMemoryDatasource::class)
    registerBean(UUIDGenerator::class)
    registerBean(LocalDateProvider::class)
    registerBean(InMemoryUserRepository::class)
    registerBean(SocialNetworkService::class)
    init()
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
