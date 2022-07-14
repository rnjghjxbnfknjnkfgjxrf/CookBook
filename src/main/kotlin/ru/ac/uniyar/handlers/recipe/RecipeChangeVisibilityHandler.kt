package ru.ac.uniyar.handlers.recipe

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
import ru.ac.uniyar.domain.queries.RecipeFetchException
import ru.ac.uniyar.domain.queries.recipe.RecipeFetchQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeChangeVisibilityQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.RecipeChangeVisibilityViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowRecipeChangeVisibilityForm(
    private val recipeFetchQuery: RecipeFetchQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val htmlView: ContextAwareViewRender,
): HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.recipeHide)
            return Response(UNAUTHORIZED)
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val recipe = try {
            recipeFetchQuery(id)
        }catch (_: RecipeFetchException){
            return Response(BAD_REQUEST)
        }
        return Response(OK).with(htmlView(request) of RecipeChangeVisibilityViewModel(recipe))
    }
}

class RecipeChangeVisibilityHandler(
    private val recipeChangeVisibilityQuery: RecipeChangeVisibilityQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>,
): HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.recipeHide)
            return Response(UNAUTHORIZED)
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        try {
            recipeChangeVisibilityQuery(id)
        }catch (_: RecipeFetchException){
            return Response(BAD_REQUEST)
        }
        return Response(FOUND).header("location", "/cookbook")
    }
}
