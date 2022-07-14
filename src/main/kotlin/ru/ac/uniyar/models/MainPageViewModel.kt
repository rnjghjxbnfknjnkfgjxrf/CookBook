package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.MainPageInfo
import ru.ac.uniyar.domain.storage.Author

data class MainPageViewModel(val mainPageInfo: MainPageInfo) : ViewModel
