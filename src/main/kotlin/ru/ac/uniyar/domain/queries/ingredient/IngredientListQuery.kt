package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.Store

class IngredientListQuery(private val repository: IngredientRepository) {
    operator fun invoke() = repository.list().filter { it.accepted }
}
