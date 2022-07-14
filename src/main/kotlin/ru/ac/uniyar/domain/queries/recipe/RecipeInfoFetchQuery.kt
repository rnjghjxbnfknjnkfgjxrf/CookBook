package ru.ac.uniyar.domain.queries.recipe

import ru.ac.uniyar.domain.queries.AuthorFetchException
import ru.ac.uniyar.domain.queries.RecipeFetchException
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.IngredientRepository
import ru.ac.uniyar.domain.storage.RecipeRepository
import java.util.*

class RecipeInfoFetchQuery(
    private val recRepository: RecipeRepository,
    private val autRepository: AuthorRepository,
    private val ingRepository: IngredientRepository) {
    
    operator fun invoke(recipeID: UUID): ChosenRecipeInfo {
        val recipe = recRepository.fetch(recipeID) ?: throw  RecipeFetchException()
        val author = autRepository.fetch(recipe.authorId) ?: throw AuthorFetchException()
        val ingredients = ingRepository.list().filter { recipe.ingredientList.contains(it.id) }
        return ChosenRecipeInfo(recipe, author, ingredients)
    }
}
