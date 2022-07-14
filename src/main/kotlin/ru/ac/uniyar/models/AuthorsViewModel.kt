package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.models.Paginator

data class AuthorsViewModel(
    val authorList : List<Author>,
    val paginator: Paginator,
    val fromRecipeCount : Int,
    val toRecipeCount : Int,) : ViewModel
