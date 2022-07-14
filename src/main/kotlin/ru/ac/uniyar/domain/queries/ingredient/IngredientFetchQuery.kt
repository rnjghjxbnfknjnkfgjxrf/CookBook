package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.storage.IngredientRepository
import java.util.UUID

class IngredientFetchQuery(
    private val repository: IngredientRepository,
) {
    operator fun invoke(ingredientId: UUID) = repository.fetch(ingredientId) ?: throw IngredientFetchException()
}
