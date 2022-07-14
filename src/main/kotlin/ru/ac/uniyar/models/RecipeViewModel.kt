package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.recipe.ChosenRecipeInfo

data class RecipeViewModel(val recipe: ChosenRecipeInfo) : ViewModel
