package ru.ac.uniyar.handlers.author

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.AuthorFetchException
import ru.ac.uniyar.domain.queries.author.AuthorFetchQuery
import ru.ac.uniyar.handlers.lensOrDefault
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.AuthorViewModel
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ChosenAuthorHandler(
    private val authorFetchQuery: AuthorFetchQuery,
    private val htmlView : ContextAwareViewRender,
) : HttpHandler {
    companion object{
        private val idLens = Path.uuid().of("id")
        private val pageLens = Query.int().defaulted("page", 1)
    }

    override fun invoke(request: Request): Response {
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val page = lensOrDefault(pageLens, request, 1)
        return try {
            val authorInfo = authorFetchQuery.invoke(id, page)
            val pagedRecipeList = authorInfo.recipeList
            val paginator = Paginator(pagedRecipeList.pageCount, page, request.uri)
            Response(OK).with(htmlView(request) of AuthorViewModel(authorInfo, paginator))
        }catch (_: AuthorFetchException){
            Response(BAD_REQUEST)
        }
    }
}
