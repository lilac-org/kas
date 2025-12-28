package com.lilac.kas.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String?,
    val success: Boolean = false,
    val data: Nothing? = null
)