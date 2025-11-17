package com.sargis.composekmp

class Greeting {
    val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}