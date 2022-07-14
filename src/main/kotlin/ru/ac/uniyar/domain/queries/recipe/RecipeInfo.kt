package ru.ac.uniyar.domain.queries.recipe

import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.Recipe

data class RecipeInfo(
    val recipe: Recipe,
    val author : Author,
)
