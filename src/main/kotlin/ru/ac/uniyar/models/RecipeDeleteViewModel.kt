package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Recipe

data class RecipeDeleteViewModel(val recipe: Recipe): ViewModel
