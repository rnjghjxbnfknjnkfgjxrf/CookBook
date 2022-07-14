package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Ingredient

data class IngredientAcceptViewModel(val form: WebForm = WebForm(), val ingredient: Ingredient?) : ViewModel
