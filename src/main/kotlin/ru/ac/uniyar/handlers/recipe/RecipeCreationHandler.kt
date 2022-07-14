package ru.ac.uniyar.handlers.recipe

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.BiDiLens
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.queries.CookingStepsException
import ru.ac.uniyar.domain.queries.CookingTimeException
import ru.ac.uniyar.domain.queries.RecipeDescriptionException
import ru.ac.uniyar.domain.queries.RecipeNameException
import ru.ac.uniyar.domain.queries.author.AuthorsListQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientListQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeCreationQuery
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.models.LoginOfferViewModel
import ru.ac.uniyar.models.RecipeCreationViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender
import java.util.UUID

class ShowRecipeCreationForm(
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val ingredientListQuery: IngredientListQuery,
    private val htmlView: ContextAwareViewRender,
) : HttpHandler{

    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.recipeCreation)
            return Response(UNAUTHORIZED)
        return Response(OK).with(htmlView(request) of RecipeCreationViewModel(
            ingredientListQuery.invoke().withIndex(),
            ingredientListQuery.invoke().size)
        )
    }
}

class RecipeCreationHandler(
    private val currentAuthorLens: RequestContextLens<Author?>,
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val ingredientListQuery: IngredientListQuery,
    private val recipeCreationQuery: RecipeCreationQuery,
    private val htmlView: ContextAwareViewRender,
) : HttpHandler{
    companion object{

        private val nameFormLens = FormField.nonEmptyString().required("name")
        private val recipeDescriptionFormLens = FormField.nonEmptyString().required("recipeDescription")
        private val ingredientListFormLens = FormField.uuid().multi.required("ingredientId")
        private val cookingStepsFormLens = FormField.nonEmptyString().required("cookingSteps")
        private val approximateCookingTimeFormLens = FormField.int().required("approximateCookingTime")
        private val recipeCreationFormLens = Body.webForm(
            Validator.Feedback,
            nameFormLens,
            recipeDescriptionFormLens,
            ingredientListFormLens,
            cookingStepsFormLens,
            approximateCookingTimeFormLens,
        ).toLens()
    }

    @Suppress("ReturnCount")
    override fun invoke(request: Request): Response {
        val permissions = permissionsLens(request)
        if (!permissions.recipeCreation)
            return Response(UNAUTHORIZED)
        var webForm = recipeCreationFormLens(request)
        val currentAuthor = currentAuthorLens(request)!!
        if (webForm.errors.isEmpty()){
            try {
                recipeCreationQuery.invoke(
                    currentAuthor.id,
                    nameFormLens(webForm),
                    recipeDescriptionFormLens(webForm),
                    ingredientListFormLens(webForm) as MutableList<UUID>,
                    cookingStepsFormLens(webForm),
                    approximateCookingTimeFormLens(webForm),
                )
                return Response(FOUND).header("location", "/cookbook")
            }catch (_: CookingTimeException){
                val newErrors = webForm.errors + Invalid(
                    approximateCookingTimeFormLens.meta.copy(description = "invalid cooking time"))
                webForm = webForm.copy(errors = newErrors)
            }catch (_: RecipeNameException){
                val newErrors = webForm.errors + Invalid(
                    nameFormLens.meta.copy(description = "invalid recipe name"))
                webForm = webForm.copy(errors = newErrors
                )
            }catch (_: RecipeDescriptionException){
                val newErrors = webForm.errors + Invalid(
                    recipeDescriptionFormLens.meta.copy(description = "invalid recipe description"))
                webForm = webForm.copy(errors = newErrors)
            }catch (_: CookingStepsException){
                val newErrors = webForm.errors + Invalid(
                    cookingStepsFormLens.meta.copy(description = "invalid cooking time"))
                webForm = webForm.copy(errors = newErrors)
            }
        }
        return Response(OK).with(htmlView(request) of RecipeCreationViewModel(
            ingredientListQuery.invoke().withIndex(),
            ingredientListQuery.invoke().size,
            webForm)
        )
    }

}
