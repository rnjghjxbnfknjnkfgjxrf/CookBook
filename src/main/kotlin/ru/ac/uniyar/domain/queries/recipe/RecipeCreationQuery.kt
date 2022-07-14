package ru.ac.uniyar.domain.queries.recipe

import ru.ac.uniyar.domain.queries.AuthorFetchException
import ru.ac.uniyar.domain.queries.CookingStepsException
import ru.ac.uniyar.domain.queries.CookingTimeException
import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.queries.RecipeDescriptionException
import ru.ac.uniyar.domain.queries.RecipeNameException
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.EMPTY_UUID
import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.Recipe
import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.Settings
import java.util.UUID

class RecipeCreationQuery(
    private val recRepository: RecipeRepository,
    private val autRepository: AuthorRepository,
    private val ingRepository: IngredientRepository,
) {

    @Suppress("LongParameterList", "ThrowsCount")
    operator fun invoke(
        authorId : UUID,
        name : String,
        recipeDescription : String,
        ingredientList : MutableList<UUID>,
        cookingSteps : String,
        approximateCookingTime : Int,
    ): UUID{
        for (ingredientId in ingredientList){
            if (ingRepository.fetch(ingredientId) == null)
                throw IngredientFetchException()
        }
        autRepository.fetch(authorId) ?: throw AuthorFetchException()
        if (approximateCookingTime <= 0)
            throw CookingTimeException()
        if (name.toDoubleOrNull() != null)
            throw RecipeNameException()
        if (recipeDescription.toDoubleOrNull() != null)
            throw RecipeDescriptionException()
        if (cookingSteps.toDoubleOrNull() != null)
            throw CookingStepsException()

        return recRepository.add(
            Recipe(EMPTY_UUID, authorId, name, recipeDescription, ingredientList, cookingSteps, approximateCookingTime)
        )
    }
}
