package ru.ac.uniyar.domain.queries.author

import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.PagedResult
import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.countPageNumbers
import ru.ac.uniyar.domain.storage.subListOrEmpty

class AuthorsQuery(
    private val autRepository: AuthorRepository,
    private val recRepository: RecipeRepository,
) {
    companion object {
        const val PAGE_SIZE = 4
    }

    operator fun invoke(pageNumber: Int, fromRecipeCount: Int?, toRecipeCount: Int?): PagedResult<Author> {
        val authorsList = autRepository.list().sortedBy { it.uploadDate }
        val recipeList = recRepository.list()
        val baseFrom = fromRecipeCount ?: authorsList
            .groupBy { author -> recipeList.filter { it.authorId == author.id } }
            .minOf { (k, v) -> k.size }
        val baseTo = toRecipeCount ?: authorsList
            .groupBy { author -> recipeList.filter { it.authorId == author.id } }
            .maxOf { (k, v) -> k.size }
        val requiredAuthors = authorsList
            .groupBy { author -> recipeList.filter { it.authorId == author.id } }
            .filter { (k, v) -> k.size in baseFrom..baseTo }
            .values.flatten()
        val filteredList = authorsList.filter { requiredAuthors.contains(it) }
        val subList = filteredList.subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        return PagedResult(subList, countPageNumbers(filteredList.size, PAGE_SIZE))
    }
}
