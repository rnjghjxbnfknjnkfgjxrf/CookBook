package ru.ac.uniyar.handlers.ingredient

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.queries.ingredient.IngredientDeleteQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientFetchQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.IngredientDeleteViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowIngredientDeleteForm(
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val ingredientFetchQuery: IngredientFetchQuery,
    private val htmlView: ContextAwareViewRender,
): HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.ingredientEdit)
            return Response(UNAUTHORIZED)
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val ingredient = try {
            ingredientFetchQuery.invoke(id)
        }catch (_: IngredientFetchException){
            return Response(BAD_REQUEST)
        }
        return Response(OK).with(htmlView(request) of IngredientDeleteViewModel(ingredient))
    }
}

class IngredientDeleteHandler(
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val ingredientDeleteQuery: IngredientDeleteQuery,
): HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.ingredientEdit)
            return Response(UNAUTHORIZED)
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        try {
            ingredientDeleteQuery(id)
        }catch (_: IngredientFetchException){
            return Response(BAD_REQUEST)
        }
        return Response(FOUND).header("location","/ingredients")
    }
}
