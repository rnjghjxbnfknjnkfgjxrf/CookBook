package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.RecipeRepository
import java.util.UUID

class IngredientDeleteQuery(
    private val ingRepository: IngredientRepository,
    private val recRepository: RecipeRepository,
) {
   operator fun invoke(id: UUID) {
       ingRepository.fetch(id) ?: throw IngredientFetchException()
       ingRepository.remove(id)
       recRepository.list().forEach { recipe -> recipe.ingredientList.remove(id) }
   }
}
