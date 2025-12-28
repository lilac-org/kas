package com.lilac.kas.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    val appModule = module {
        single { loadAppConfig() }
        single { loadAuthConfig() }
    }

    install(Koin) {
        slf4jLogger()
        modules(
            appModule,
        )
    }
}