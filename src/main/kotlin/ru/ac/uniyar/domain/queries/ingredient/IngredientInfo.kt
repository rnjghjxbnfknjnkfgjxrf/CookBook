package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.domain.storage.PagedResult
import ru.ac.uniyar.domain.storage.Recipe

data class IngredientInfo(
    val ingredient: Ingredient,
    val recipeList: PagedResult<Recipe>
)
