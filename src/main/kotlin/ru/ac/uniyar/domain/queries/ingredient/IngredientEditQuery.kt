package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.queries.CaloriesValueException
import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.queries.IngredientNameException
import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.domain.storage.IngredientRepository
import java.time.LocalDate
import java.util.UUID

class IngredientEditQuery(
    private val repository: IngredientRepository
) {

    @Suppress("ThrowsCount")
    operator fun invoke(id: UUID, name: String, calories: Int) {
        if (name.contains("[0-9]".toRegex()))
            throw IngredientNameException()
        if (calories < 0)
            throw CaloriesValueException()
        val ingredient = repository.remove(id) ?: throw IngredientFetchException()
        repository.add(
            ingredient.copy(
                name = name,
                calories = calories,
                ))
    }
}
