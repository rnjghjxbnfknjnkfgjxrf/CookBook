package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.ac.uniyar.domain.queries.MainPageQuery
import ru.ac.uniyar.models.MainPageViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class MainPageHandler(
    private val mainPageQuery: MainPageQuery,
    private val htmlView : ContextAwareViewRender,
) : HttpHandler{
    override fun invoke(request: Request): Response {
        val mainPageInfo = mainPageQuery.invoke()
        val model = MainPageViewModel(mainPageInfo)
        return Response(OK).with(htmlView(request) of model)
    }
}
