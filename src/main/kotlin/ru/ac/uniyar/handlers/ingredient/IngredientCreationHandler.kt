package ru.ac.uniyar.handlers.ingredient

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.CaloriesValueException
import ru.ac.uniyar.domain.queries.IngredientNameException
import ru.ac.uniyar.domain.queries.ingredient.IngredientCreationQuery
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.models.IngredientCreationViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowIngredientCreationForm(
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val htmlView : ContextAwareViewRender,
    ) : HttpHandler{
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.ingredientCreation)
            return Response(UNAUTHORIZED)
        return Response(OK).with(htmlView(request) of IngredientCreationViewModel())
    }
}

class IngredientCreationHandler(
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val ingredientCreationQuery: IngredientCreationQuery,
    private val htmlView : ContextAwareViewRender,
) : HttpHandler{
    companion object{
        private val nameFormLens = FormField.nonEmptyString().required("name")
        private val caloriesFormLens = FormField.int().required("calories")
        private val ingredientCreationFormLens = Body.webForm(
            Validator.Feedback,
            nameFormLens,
            caloriesFormLens,
        ).toLens()
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.ingredientCreation)
            return Response(UNAUTHORIZED)
        var webForm = ingredientCreationFormLens(request)
        if (webForm.errors.isEmpty()){
            try {
                ingredientCreationQuery.invoke(
                    nameFormLens(webForm),
                    caloriesFormLens(webForm),
                    permissions.ingredientAccept,
                )
                return Response(Status.FOUND).header("location", "/ingredients")
            }catch (_: IngredientNameException){
                val newErrors = webForm.errors + Invalid(
                    nameFormLens.meta.copy(description = "invalid name"))
                webForm = webForm.copy(errors = newErrors)
            }catch (_: CaloriesValueException){
                val newErrors = webForm.errors + Invalid(
                    caloriesFormLens.meta.copy(description = "invalid calories value"))
                webForm = webForm.copy(errors = newErrors)
            }
        }
        return Response(OK).with(htmlView(request) of IngredientCreationViewModel(webForm))
    }
}
