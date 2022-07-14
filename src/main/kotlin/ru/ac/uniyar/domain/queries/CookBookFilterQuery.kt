package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.RecipeRepository
import java.util.UUID

class CookBookFilterQuery(private val repository: RecipeRepository) {

    operator fun invoke(showHidden: Boolean, currentAuthorId: UUID?) : CookBookFilterInfo {
        val fromIngredientCount = repository.list()
            .filter { it.authorId == currentAuthorId || showHidden || !it.hidden }
            .minOfOrNull { it.ingredientList.size } ?: 0
        val toIngredientCount = repository.list()
            .filter { it.authorId == currentAuthorId || showHidden || !it.hidden }
            .maxOfOrNull { it.ingredientList.size } ?: 0
        return CookBookFilterInfo(fromIngredientCount, toIngredientCount)
    }
}
