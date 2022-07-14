package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.ingredient.IngredientInfo
import ru.ac.uniyar.domain.storage.Author

data class IngredientViewModel(
    val ingredientInfo : IngredientInfo,
    val paginator: Paginator,) : ViewModel
