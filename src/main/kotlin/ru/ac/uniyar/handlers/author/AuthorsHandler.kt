package ru.ac.uniyar.handlers.author


import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.int
import ru.ac.uniyar.domain.queries.author.AuthorsFilterQuery
import ru.ac.uniyar.domain.queries.author.AuthorsQuery
import ru.ac.uniyar.handlers.lensOrDefault
import ru.ac.uniyar.models.AuthorsViewModel
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.template.ContextAwareViewRender

class AuthorsHandler(
    private val authorsFilterQuery: AuthorsFilterQuery,
    private val authorsQuery: AuthorsQuery,
    private val htmlView : ContextAwareViewRender,
) : HttpHandler {
    companion object{
        private val pageLens = Query.int().defaulted("page", 1)
        private val fromLens = Query.int().optional("fromRecipeCount")
        private val toLens = Query.int().optional("toRecipeCount")
    }

    override fun invoke(request: Request): Response {
        val page = lensOrDefault(pageLens, request, 1)
        val authorsFilterInfo = authorsFilterQuery.invoke()
        val fromRecipeCount = lensOrDefault(fromLens, request, authorsFilterInfo.fromRecipeCount)
        val toRecipeCount = lensOrDefault(toLens, request, authorsFilterInfo.toRecipeCount)
        val pagedResult = authorsQuery.invoke(page, fromRecipeCount, toRecipeCount)
        val paginator = Paginator(pagedResult.pageCount, page, request.uri)
        val model = AuthorsViewModel(pagedResult.values, paginator, fromRecipeCount, toRecipeCount)
        return Response(OK).with(htmlView(request) of model)
    }
}
