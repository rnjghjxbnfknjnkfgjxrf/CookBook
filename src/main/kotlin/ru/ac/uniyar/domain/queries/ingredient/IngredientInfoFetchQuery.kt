package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.PagedResult
import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.countPageNumbers
import ru.ac.uniyar.domain.storage.subListOrEmpty
import java.util.*

class IngredientInfoFetchQuery(
    private val ingRepository: IngredientRepository,
    private val recRepository: RecipeRepository,
) {
    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(ingredientId: UUID, pageNumber: Int, showHidden: Boolean) : IngredientInfo {
        val ingredient = ingRepository.fetch(ingredientId) ?: throw IngredientFetchException()
        if (!(ingredient.accepted || showHidden)) throw IngredientFetchException()
        val recipeList =  recRepository.list()
            .filter { it.ingredientList.contains(ingredientId) }
            .sortedBy { it.uploadDate }
        val recipeSubList = recipeList.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        val pagedRecipeList = PagedResult(recipeSubList, countPageNumbers(recipeList.size, PAGE_SIZE))
        return IngredientInfo(ingredient, pagedRecipeList)
    }
}

