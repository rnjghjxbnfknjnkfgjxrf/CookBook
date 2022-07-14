package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.PagedResult
import ru.ac.uniyar.domain.storage.countPageNumbers
import ru.ac.uniyar.domain.storage.subListOrEmpty

class IngredientsQuery(private val repository: IngredientRepository) {

    companion object{
        const val PAGE_SIZE = 5
    }

    operator fun invoke(
        pageNumber: Int,
        fromCalories: Int?,
        toCalories: Int?,
        showHidden: Boolean)
    : PagedResult<Ingredient> {
        val list= repository.list().filter { it.accepted || showHidden }.sortedBy { it.uploadDate }
        val baseFrom = fromCalories ?: list.minOf { it.calories }
        val baseTo = toCalories ?: list.maxOf { it.calories }
        val filteredList = list.filter { it.calories in baseFrom..baseTo }
        val subList = filteredList.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        return PagedResult(subList, countPageNumbers(filteredList.size, PAGE_SIZE))
    }
}
