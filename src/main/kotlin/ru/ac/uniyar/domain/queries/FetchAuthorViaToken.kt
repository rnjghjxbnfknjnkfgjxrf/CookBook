package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.Store
import java.util.UUID

class FetchAuthorViaToken(private val autRepository: AuthorRepository) {

    operator fun invoke(token: String): Author? {
        val uuid = try {
            UUID.fromString(token)
        }catch (_: IllegalArgumentException){
            return null
        }
        return autRepository.fetch(uuid)
    }
}
