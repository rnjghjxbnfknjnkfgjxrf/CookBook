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
import ru.ac.uniyar.domain.queries.recipe.RecipeDeleteQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeFetchQuery
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.RecipeDeleteViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowRecipeDeleteForm(
    private val currentAuthorLens: RequestContextLens<Author?>,
    private val recipeFetchQuery: RecipeFetchQuery,
    private val htmlView: ContextAwareViewRender,
): HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val recipe = try {
            recipeFetchQuery.invoke(id)
        } catch (_: RecipeFetchException){
            return Response(BAD_REQUEST)
        }
        val currentAuthor = currentAuthorLens(request) ?: return Response(UNAUTHORIZED)
        if (recipe.authorId != currentAuthor.id)
            return Response(UNAUTHORIZED)
        return Response(OK).with(htmlView(request) of RecipeDeleteViewModel(recipe))
    }
}

class RecipeDeleteHandler(
    private val currentAuthorLens: RequestContextLens<Author?>,
    private val recipeFetchQuery: RecipeFetchQuery,
    private val recipeDeleteQuery: RecipeDeleteQuery,
): HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val recipe = try {
            recipeFetchQuery.invoke(id)
        } catch (_: RecipeFetchException){
            return Response(BAD_REQUEST)
        }
        val currentAuthor = currentAuthorLens(request) ?: return Response(UNAUTHORIZED)
        if (recipe.authorId != currentAuthor.id)
            return Response(UNAUTHORIZED)
        recipeDeleteQuery(id)
        return Response(FOUND).header("location", "/cookbook")
    }
}
