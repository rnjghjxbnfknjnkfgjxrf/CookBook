package ru.ac.uniyar.domain.queries.author

import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.PagedResult
import ru.ac.uniyar.domain.storage.Recipe

data class AuthorInfo(
    val author: Author,
    val recipeList: PagedResult<Recipe>,
)
