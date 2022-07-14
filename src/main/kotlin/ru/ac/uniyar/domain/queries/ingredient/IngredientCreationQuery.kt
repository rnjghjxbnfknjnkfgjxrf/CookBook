package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.queries.CaloriesValueException
import ru.ac.uniyar.domain.queries.IngredientNameException
import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.Store
import java.util.*

class IngredientCreationQuery(private val repository: IngredientRepository) {

    operator fun invoke(
        name: String,
        calories: Int,
        accepted: Boolean
    ): UUID{
        if (name.contains("[0-9]".toRegex()))
            throw IngredientNameException()
        if (calories < 0)
            throw CaloriesValueException()
        return  repository.add(
            Ingredient(
                EMPTY_UUID,
                name,
                calories,
                accepted = accepted,
            )
        )
    }
}
