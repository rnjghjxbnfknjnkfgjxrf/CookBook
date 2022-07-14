package ru.ac.uniyar.models

import org.http4k.core.Uri
import org.http4k.template.ViewModel

data class ShowErrorInfoViewModel(val uri: Uri) : ViewModel
