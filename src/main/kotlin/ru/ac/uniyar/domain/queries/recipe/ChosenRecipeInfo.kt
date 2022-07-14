package ru.ac.uniyar.domain.queries.recipe

import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.domain.storage.Recipe

data class ChosenRecipeInfo(
    val recipe : Recipe,
    val author : Author,
    val ingredientList : List<Ingredient>,
)
