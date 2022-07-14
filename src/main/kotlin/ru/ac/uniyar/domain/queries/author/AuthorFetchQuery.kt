package ru.ac.uniyar.domain.queries.author

import ru.ac.uniyar.domain.queries.AuthorFetchException
import ru.ac.uniyar.domain.storage.AuthorRepository
import ru.ac.uniyar.domain.storage.PagedResult
import ru.ac.uniyar.domain.storage.RecipeRepository
import ru.ac.uniyar.domain.storage.countPageNumbers
import ru.ac.uniyar.domain.storage.subListOrEmpty
import java.util.*

class AuthorFetchQuery(
    private val autRepository: AuthorRepository,
    private val recRepository: RecipeRepository
) {

    companion object {
        const val PAGE_SIZE = 3
    }

    operator fun invoke(authorId: UUID, pageNumber: Int): AuthorInfo {
        val author = autRepository.fetch(authorId) ?: throw AuthorFetchException()
        val authorsRecipeList = recRepository.list()
            .filter { it.authorId == authorId }
            .sortedBy { it.uploadDate }
        val authorsRecipeSubList = authorsRecipeList
            .subListOrEmpty((pageNumber - 1) * PAGE_SIZE, pageNumber * PAGE_SIZE)
        val pagedRecipeList = PagedResult(authorsRecipeSubList, countPageNumbers(authorsRecipeList.size, PAGE_SIZE))
        return AuthorInfo(author, pagedRecipeList)
    }
}
