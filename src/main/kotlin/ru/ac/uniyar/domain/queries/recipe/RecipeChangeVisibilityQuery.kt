package ru.ac.uniyar.domain.queries.recipe

import ru.ac.uniyar.domain.queries.RecipeFetchException
import ru.ac.uniyar.domain.storage.RecipeRepository
import java.util.UUID

class RecipeChangeVisibilityQuery(
    private val repository: RecipeRepository
) {

    operator fun invoke(id: UUID) {
        val recipe = repository.remove(id) ?: throw RecipeFetchException()
        repository.add(
            recipe.copy(
                hidden = !recipe.hidden
            )
        )
    }
}
