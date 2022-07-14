package ru.ac.uniyar.domain.queries.computations

fun hashPassword(password: String, salt: String): String{
    val saltedPassword = password + salt
    return Checksum(saltedPassword.toByteArray()).toString()
}
