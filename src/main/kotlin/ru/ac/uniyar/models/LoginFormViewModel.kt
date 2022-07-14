package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.storage.Author

data class LoginFormViewModel(val form : WebForm = WebForm()) : ViewModel
