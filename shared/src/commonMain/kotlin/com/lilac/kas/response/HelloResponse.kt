package com.lilac.kas.response

import kotlinx.serialization.Serializable

@Serializable
data class HelloResponse(
    val success: Boolean = true,
    val message: String? = "Hello from Kas Backend. Be Rich!",
    val data: Nothing? = null
)