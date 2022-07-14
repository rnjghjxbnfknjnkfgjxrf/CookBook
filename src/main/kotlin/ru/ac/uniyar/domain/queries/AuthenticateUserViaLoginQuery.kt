package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.queries.computations.hashPassword
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.Settings

class AuthenticateUserViaLoginQuery(
    private val usersRepository: AuthorRepository,
    private val settings: Settings,
) {

    operator fun invoke(nickName: String, password: String): String {
        val user = usersRepository.list().find { it.nickName == nickName } ?: throw AuthenticationException()
        val hashedPassword = hashPassword(password, settings.salt)
        if (hashedPassword != user.password)
            throw AuthenticationException()
        return user.id.toString()
    }
}
