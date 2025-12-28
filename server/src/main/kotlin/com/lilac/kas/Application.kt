package com.lilac.kas

import com.lilac.kas.config.configureCors
import com.lilac.kas.config.configureHealth
import com.lilac.kas.config.configureKoin
import com.lilac.kas.config.configureLogging
import com.lilac.kas.config.configureResponse
import com.lilac.kas.config.configureRouting
import com.lilac.kas.config.configureSecurity
import com.lilac.kas.config.configureSerialization
import com.lilac.kas.db.DatabaseFactory.configureDatabase
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureKoin()
    configureLogging()
    configureSerialization()
    configureResponse()
    configureCors()
    configureSecurity()
    configureHealth()
    configureRouting()
}