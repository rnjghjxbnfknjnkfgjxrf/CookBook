package ru.ac.uniyar.models

import org.http4k.template.ViewModel

data class UniqueIngredientsModel(val uniqueIngredients: Iterable<IndexedValue<String>>) : ViewModel
