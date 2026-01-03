package com.lilac.kas.config

import com.lilac.kas.config.AppConstant.JWT_NAME
import com.lilac.kas.presentation.routes.identityRoutes
import com.lilac.kas.presentation.response.HelloResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/") { call.respondRedirect("/api") }
        route("/api") {
            openAPI(path = "openapi", swaggerFile = "openapi/documentation.json")
            swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.json")

            get {
                call.respond(
                    HttpStatusCode.OK,
                    HelloResponse()
                )
            }
            identityRoutes()

            authenticate(JWT_NAME) {
                get("/test-auth") {
                    call.respond(HttpStatusCode.OK, "Hello World!")
                }
            }
        }
    }
}