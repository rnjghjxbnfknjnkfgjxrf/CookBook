package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.domain.storage.Recipe

data class RecipeEditViewModel(
    val recipe: Recipe,
    val ingredientList: Iterable<IndexedValue<Ingredient>>,
    val ingredientListSize: Int,
    val form: WebForm = WebForm()
): ViewModel
