package ru.ac.uniyar.handlers.ingredient



import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.nonEmptyString
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.queries.IngredientNameException
import ru.ac.uniyar.domain.queries.ingredient.IngredientAcceptQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientFetchQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.IngredientAcceptViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowIngredientAcceptForm(
    private val ingredientFetchQuery: IngredientFetchQuery,
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val htmlView: ContextAwareViewRender,
): HttpHandler {
    companion object{
        private val idLens = Path.uuid().of("id")
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.ingredientAccept)
            return Response(UNAUTHORIZED)
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val ingredient = try {
            ingredientFetchQuery(id)
        }catch (_: IngredientFetchException){
            return Response(BAD_REQUEST)
        }
        return Response(OK).with(htmlView(request) of IngredientAcceptViewModel(ingredient = ingredient))
    }
}


class IngredientAcceptHandler(
    private val ingredientFetchQuery: IngredientFetchQuery,
    private val ingredientAcceptQuery: IngredientAcceptQuery,
    private val htmlView: ContextAwareViewRender,
    private val permissionsLens: RequestContextLens<RolePermissions>
): HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
        private val nameFormLens = FormField.nonEmptyString().required("name")
        private val ingredientAcceptFormLens = Body.webForm(
            Validator.Feedback,
            nameFormLens,
        ).toLens()
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.ingredientAccept)
            return Response(UNAUTHORIZED)
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val ingredient = try {
            ingredientFetchQuery(id)
        }catch (_: IngredientNameException){
            return Response(BAD_REQUEST)
        }
        var webForm = ingredientAcceptFormLens(request)
        if (webForm.errors.isEmpty()){
            try {
                ingredientAcceptQuery.invoke(
                    id, nameFormLens(webForm),)
                return Response(FOUND).header("location", "/ingredients")
            }catch (_: IngredientNameException){
                val newErrors = webForm.errors + Invalid(
                    nameFormLens.meta.copy(description = "invalid name")
                )
                webForm = webForm.copy(errors = newErrors)
            }
        }
        return Response(OK).with(htmlView(request) of IngredientAcceptViewModel(webForm, ingredient))
    }
}
