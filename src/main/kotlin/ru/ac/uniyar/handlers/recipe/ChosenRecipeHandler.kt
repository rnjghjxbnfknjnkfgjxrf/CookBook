package ru.ac.uniyar.handlers.recipe

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.uuid
import ru.ac.uniyar.domain.queries.AuthorFetchException
import ru.ac.uniyar.domain.queries.RecipeFetchException
import ru.ac.uniyar.domain.queries.recipe.RecipeInfoFetchQuery
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.RecipeViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ChosenRecipeHandler(
    private val recipeInfoFetchQuery: RecipeInfoFetchQuery,
    private val htmlView: ContextAwareViewRender,
) : HttpHandler{
    companion object{
        val idLens = Path.uuid().of("id")
    }

    override fun invoke(request: Request): Response {
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        return try{
            val recipe = recipeInfoFetchQuery.invoke(id)
            Response(OK).with(htmlView(request) of RecipeViewModel(recipe))
        }catch (_: RecipeFetchException){
            Response(BAD_REQUEST)
        }catch (_: AuthorFetchException){
            Response(BAD_REQUEST)
        }
    }

}
