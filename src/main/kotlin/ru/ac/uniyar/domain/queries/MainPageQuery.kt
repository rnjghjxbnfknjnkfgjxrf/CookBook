package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.Store

class MainPageQuery(
    private val recRepository: RecipeRepository,
    private val ingRepository: IngredientRepository
) {

    companion object{
        const val COMPLEX_LIST_SIZE = 5
    }

    operator fun invoke() : MainPageInfo {
        val totalRecipeCount = recRepository.list().size
        val complexRecipeCount = recRepository.list().filter { it.ingredientList.size > COMPLEX_LIST_SIZE }.size
        val mostFrequentIngredient = recRepository.list()
            .map { it.ingredientList }.flatten()
            .map { ingRepository.fetch(it)!! }
            .groupBy { it.name }.toList()
            .sortedBy { (k, v) -> v.size }.reversed()
            .toMap()
        val key = if (recRepository.list().isNotEmpty()){
            mostFrequentIngredient.entries.iterator().next().key
        }else{
            ""
        }

        return  MainPageInfo(totalRecipeCount, complexRecipeCount, key)
    }
}
