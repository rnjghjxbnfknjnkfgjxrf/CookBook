package ru.ac.uniyar.handlers.ingredient

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.RequestContextLens
import org.http4k.lens.int
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.queries.ingredient.IngredientInfoFetchQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.handlers.lensOrDefault
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.IngredientViewModel
import ru.ac.uniyar.models.Paginator
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ChosenIngredientHandler(
    private val ingredientInfoFetchQuery: IngredientInfoFetchQuery,
    private val htmlView: ContextAwareViewRender,
    private val permissionsLens: RequestContextLens<RolePermissions>,
) : HttpHandler{
    companion object {
        private val idLens = Path.uuid().of("id")
        private val pageLens = Query.int().defaulted("page", 1)
    }

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val page = lensOrDefault(pageLens, request, 1)
        return try {
            val ingredientInfo = ingredientInfoFetchQuery.invoke(id, page, permissions.ingredientAccept)
            val pagedRecipeList = ingredientInfo.recipeList
            val paginator = Paginator(pagedRecipeList.pageCount, page, request.uri)
            Response(OK).with(htmlView(request) of IngredientViewModel(ingredientInfo, paginator))
        }catch (_: IngredientFetchException){
            Response(BAD_REQUEST)
        }
    }
}
