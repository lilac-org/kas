package com.lilac.kas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform