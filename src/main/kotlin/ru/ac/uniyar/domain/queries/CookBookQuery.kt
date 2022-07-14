package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.queries.recipe.RecipeInfo
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.PagedResult
import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.Store
import ru.ac.uniyar.domain.storage.countPageNumbers
import ru.ac.uniyar.domain.storage.subListOrEmpty

class CookBookQuery(
    private val recRepository: RecipeRepository,
    private val autRepository: AuthorRepository) {

    companion object{
        const val PAGE_SIZE = 3
    }

    operator fun invoke(
        pageNumber : Int,
        fromIngredientCount : Int?,
        toIngredientCount : Int?
    ): PagedResult<RecipeInfo> {
        val list = recRepository.list().sortedBy { it.uploadDate }
        val baseFrom = fromIngredientCount ?: list.minOf { it.ingredientList.size }
        val baseTo = toIngredientCount ?: list.maxOf { it.ingredientList.size }
        val filteredList = list.filter { it.ingredientList.size in baseFrom..baseTo }
        val subList = filteredList.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        val infoList = subList.map { recipe ->
            RecipeInfo(
                recipe,
                autRepository.fetch(recipe.authorId)!!
            )
        }
        return PagedResult(infoList, countPageNumbers(filteredList.size, PAGE_SIZE))
    }
}
