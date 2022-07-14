package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.RequestContextLens
import org.http4k.lens.int
import ru.ac.uniyar.domain.queries.CookBookFilterQuery
import ru.ac.uniyar.domain.queries.CookBookQuery
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.models.CookBookViewModel
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.template.ContextAwareViewRender

class CookBookHandler(
    private val cookBookFilterQuery: CookBookFilterQuery,
    private val cookBookQuery: CookBookQuery,
    private val htmlView : ContextAwareViewRender,
    private val currentAuthorLens: RequestContextLens<Author?>,
    private val permissionsLens: RequestContextLens<RolePermissions>
) : HttpHandler{
    companion object {
        private val pageLens = Query.int().defaulted("page", 1)
        private val fromLens = Query.int().optional("fromIngredientCount")
        private val toLens = Query.int().optional("toIngredientCount")
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        val currentAuthorId = currentAuthorLens(request)?.id
        val page = lensOrDefault(pageLens, request, 1)
        val cookBookFilterInfo = cookBookFilterQuery.invoke(permissions.recipeHide, currentAuthorId)
        val fromIngredientCount = lensOrDefault(fromLens, request, cookBookFilterInfo.fromIngredientCount)
        val toIngredientCount = lensOrDefault(toLens, request, cookBookFilterInfo.toIngredientCount)
        val pagedResult = cookBookQuery.invoke(page, fromIngredientCount, toIngredientCount)
        val paginator = Paginator(pagedResult.pageCount, page, request.uri)
        val model = CookBookViewModel(pagedResult.values, paginator, fromIngredientCount, toIngredientCount)
        return Response(OK).with(htmlView(request) of model)
    }
}
