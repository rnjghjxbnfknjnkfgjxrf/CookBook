package ru.ac.uniyar.domain.queries.author

import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.RecipeRepository

class AuthorsFilterQuery(
    private val autRepository: AuthorRepository,
    private val recRepository: RecipeRepository
) {

    operator fun invoke(): AuthorsFilterInfo {
        val authorsList = autRepository.list()
        val recipeList = recRepository.list()
        val fromRecipeCount = authorsList
            .groupBy { author -> recipeList.filter { it.authorId == author.id } }
            .minOfOrNull { (k, v) -> k.size } ?: 0
        val toRecipeCount = authorsList
            .groupBy { author -> recipeList.filter { it.authorId == author.id } }
            .maxOfOrNull { (k, v) -> k.size } ?: 0
        return AuthorsFilterInfo(fromRecipeCount, toRecipeCount)
    }
}
