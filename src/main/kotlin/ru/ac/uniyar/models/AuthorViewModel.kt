package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.author.AuthorInfo
import ru.ac.uniyar.domain.storage.Author

data class AuthorViewModel(val authorInfo: AuthorInfo, val paginator: Paginator,) : ViewModel
