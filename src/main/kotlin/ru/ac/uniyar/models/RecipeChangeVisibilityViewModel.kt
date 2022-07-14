package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Recipe

data class RecipeChangeVisibilityViewModel(val recipe: Recipe): ViewModel
