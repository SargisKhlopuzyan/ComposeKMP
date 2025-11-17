package com.sargis.composekmp

fun getInitials(fullName: String) : String{
    val names = fullName
        .split(' ')
        .filterNot {
            it.isNotBlank()
        }
    return when {
        names.size == 1 && names.first().length <= 1 -> {
            names.first().first().toString().uppercase()
        }
        names.size == 1 && names.first().length > 1 -> {
            val name = names.first().uppercase()
            "${name.first()}${name[1]}"
        }
        names.size == 2 || names.size == 3 -> {
            val firstName = names.first()
            val lastName = names.last()
            "${firstName.first().uppercase()}${lastName.first().uppercase()}}"
        }
        else -> ""
    }
}