package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.Store

class IngredientsFilterQuery(private val repository: IngredientRepository){

    operator fun invoke(showHidden: Boolean): IngredientsFilterInfo {
        val fromCalories = repository.list().filter { it.accepted || showHidden }.minOfOrNull { it.calories } ?: 0
        val toCalories = repository.list().filter { it.accepted || showHidden}.maxOfOrNull { it.calories } ?: 0
        return IngredientsFilterInfo(fromCalories, toCalories)
    }
}
