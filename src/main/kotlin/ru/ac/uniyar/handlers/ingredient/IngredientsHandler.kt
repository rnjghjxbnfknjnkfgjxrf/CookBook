package ru.ac.uniyar.handlers.ingredient

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.RequestContextLens
import org.http4k.lens.int
import ru.ac.uniyar.domain.queries.ingredient.IngredientsFilterQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientsQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.handlers.lensOrDefault
import ru.ac.uniyar.models.IngredientsViewModel
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.template.ContextAwareViewRender

class IngredientsHandler(
    private val ingredientsFilterQuery : IngredientsFilterQuery,
    private val ingredientsQuery : IngredientsQuery,
    private val htmlView : ContextAwareViewRender,
    private val permissionsLens: RequestContextLens<RolePermissions>,
) : HttpHandler{
    companion object{
        private val pageLens = Query.int().defaulted("page", 1)
        private val fromLens = Query.int().optional("fromCalories")
        private val toLens = Query.int().optional("toCalories")
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        val page = lensOrDefault(pageLens, request, 1)
        val filterInfo = ingredientsFilterQuery.invoke(permissions.ingredientAccept)
        val fromCalories = lensOrDefault(fromLens, request, filterInfo.fromCalories)
        val toCalories = lensOrDefault(toLens, request, filterInfo.toCalories)
        val pagedResult = ingredientsQuery.invoke(page, fromCalories, toCalories, permissions.ingredientAccept)
        val paginator = Paginator(pagedResult.pageCount, page, request.uri)
        val model = IngredientsViewModel(pagedResult.values, paginator, fromCalories, toCalories)
        return Response(OK).with(htmlView(request) of model)
    }
}
