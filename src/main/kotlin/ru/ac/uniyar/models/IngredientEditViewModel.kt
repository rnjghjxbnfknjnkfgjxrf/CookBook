package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Ingredient

data class IngredientEditViewModel(val ingredient: Ingredient, val form: WebForm = WebForm()): ViewModel
