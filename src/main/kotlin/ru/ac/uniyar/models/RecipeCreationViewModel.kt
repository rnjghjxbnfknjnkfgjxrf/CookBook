package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.Ingredient

data class RecipeCreationViewModel(
    val ingredientList : Iterable<IndexedValue<Ingredient>>,
    val ingredientListSize : Int,
    val form: WebForm = WebForm(),
) : ViewModel
