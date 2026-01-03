package com.lilac.kas.presentation.theme

import androidx.compose.runtime.Composable

@Composable
actual fun KasTheme(darkTheme: Boolean, content: @Composable (() -> Unit)) {
    NonAndroidKasTheme(darkTheme = darkTheme, content = content)
}