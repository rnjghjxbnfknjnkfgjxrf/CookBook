package ru.ac.uniyar.domain.queries


data class MainPageInfo(
    val totalRecipeCount : Int,
    val complexRecipeCount : Int,
    val mostFrequentIngredient : String,
)
