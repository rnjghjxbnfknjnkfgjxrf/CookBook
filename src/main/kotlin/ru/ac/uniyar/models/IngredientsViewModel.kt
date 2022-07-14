package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.models.Paginator

data class IngredientsViewModel(
    val ingredientsList: List<Ingredient>,
    val paginator : Paginator,
    val fromCalories: Int,
    val toCalories : Int,): ViewModel
