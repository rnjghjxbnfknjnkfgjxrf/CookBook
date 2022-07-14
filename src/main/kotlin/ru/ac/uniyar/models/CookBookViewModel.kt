package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.recipe.RecipeInfo
import ru.ac.uniyar.domain.storage.Author

data class CookBookViewModel(
    val recipeList: List<RecipeInfo>,
    val paginator: Paginator,
    val fromIngredientCount : Int,
    val toIngredientCount : Int,) : ViewModel
