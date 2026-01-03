package com.lilac.kas

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.lilac.kas.di.initKoin
import io.github.vinceglb.filekit.FileKit
import kas.composeapp.generated.resources.Res
import kas.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

fun main() {
    initKoin()
    FileKit.init(appId = "com.lilac.kas")

    application {
        val windowState = rememberWindowState(
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "kas",
            state = windowState,
            icon = painterResource(Res.drawable.compose_multiplatform)
        ) {
            App()
        }
    }
}