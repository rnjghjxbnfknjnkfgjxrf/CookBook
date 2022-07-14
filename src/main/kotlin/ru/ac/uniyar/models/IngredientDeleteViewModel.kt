package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Ingredient

data class IngredientDeleteViewModel(val ingredient: Ingredient): ViewModel
