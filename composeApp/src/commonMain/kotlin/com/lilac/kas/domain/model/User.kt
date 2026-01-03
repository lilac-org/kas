package com.lilac.kas.domain.model

data class User(
    val id: String,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)