package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.Store

class CookBookListQuery(private val repository: RecipeRepository) {

    operator fun invoke() = repository.list()
}
