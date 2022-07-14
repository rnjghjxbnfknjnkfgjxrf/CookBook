package ru.ac.uniyar.domain.queries.author

import ru.ac.uniyar.domain.storage.Store

class AuthorsListQuery(store: Store) {
    private val repository = store.authorRepository

    operator fun invoke() = repository.list()
}
