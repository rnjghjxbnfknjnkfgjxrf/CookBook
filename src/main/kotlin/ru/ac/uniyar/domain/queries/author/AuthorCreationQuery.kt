package ru.ac.uniyar.domain.queries.author

import ru.ac.uniyar.domain.REGISTERED_USER_ROLE
import ru.ac.uniyar.domain.queries.DescriptionException
import ru.ac.uniyar.domain.queries.EmailAddressException
import ru.ac.uniyar.domain.queries.NickNameException
import ru.ac.uniyar.domain.queries.computations.hashPassword
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.Settings
import java.util.UUID

class AuthorCreationQuery(
    private val repository: AuthorRepository,
    private val settings: Settings,
) {
    @Suppress("ThrowsCount")
    operator fun invoke(
        nickName: String,
        emailAddress: String,
        description: String,
        password: String,
    ): UUID {
        if (nickName.toDoubleOrNull() != null)
            throw NickNameException()
        if (description.toDoubleOrNull() != null)
            throw DescriptionException()
        if (!emailAddress.matches("[a-zA-Z\\d]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}".toRegex()))
            throw EmailAddressException()
        return repository.add(
            Author(
                EMPTY_UUID,
                REGISTERED_USER_ROLE.id,
                nickName,
                emailAddress,
                description,
                hashPassword(password, settings.salt)
            )
        )
    }
}
