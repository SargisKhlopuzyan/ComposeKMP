package com.sargis.composekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform