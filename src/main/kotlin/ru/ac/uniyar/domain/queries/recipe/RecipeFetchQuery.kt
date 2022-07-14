package ru.ac.uniyar.domain.queries.recipe

import ru.ac.uniyar.domain.queries.RecipeFetchException
import ru.ac.uniyar.domain.storage.Recipe
import ru.ac.uniyar.domain.storage.RecipeRepository
import java.util.UUID

class RecipeFetchQuery(
    private val repository: RecipeRepository
) {
    operator fun invoke(id: UUID) = repository.fetch(id) ?: throw RecipeFetchException()
}
