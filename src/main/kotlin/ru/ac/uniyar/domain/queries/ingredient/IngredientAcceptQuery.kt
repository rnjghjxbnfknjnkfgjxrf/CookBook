package ru.ac.uniyar.domain.queries.ingredient

import ru.ac.uniyar.domain.queries.IngredientNameException
import ru.ac.uniyar.domain.storage.Ingredient
import ru.ac.uniyar.domain.storage.IngredientRepository
import java.time.LocalDate
import java.util.UUID

class IngredientAcceptQuery(
    private val repository: IngredientRepository
) {
    operator fun invoke(id: UUID, name: String) {
        if (name.contains("[0-9]".toRegex()))
            throw IngredientNameException()
        val ingredient = repository.remove(id)!!
        repository.add(ingredient.copy(name = name, accepted = true, uploadDate = LocalDate.now()))
    }
}
