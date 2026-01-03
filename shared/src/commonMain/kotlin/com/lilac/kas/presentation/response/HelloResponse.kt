package com.lilac.kas.presentation.response

import kotlinx.serialization.Serializable

@Serializable
data class HelloResponse(
    val success: Boolean = true,
    val message: String? = "Hello from Kas. Be Rich!",
    val data: Nothing? = null
)