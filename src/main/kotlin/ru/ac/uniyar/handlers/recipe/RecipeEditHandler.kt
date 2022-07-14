package ru.ac.uniyar.handlers.recipe

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.Path
import org.http4k.lens.RequestContextLens
import org.http4k.lens.Validator
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.AuthorFetchException
import ru.ac.uniyar.domain.queries.CookingStepsException
import ru.ac.uniyar.domain.queries.CookingTimeException
import ru.ac.uniyar.domain.queries.IngredientFetchException
import ru.ac.uniyar.domain.queries.RecipeDescriptionException
import ru.ac.uniyar.domain.queries.RecipeFetchException
import ru.ac.uniyar.domain.queries.RecipeNameException
import ru.ac.uniyar.domain.queries.ingredient.IngredientListQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeEditQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeFetchQuery
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.RecipeEditViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender
import java.util.UUID

class ShowRecipeEditForm(
    private val currentAuthorLens: RequestContextLens<Author?>,
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val ingredientListQuery: IngredientListQuery,
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
            recipeFetchQuery(id)
        }catch (_: RecipeFetchException){
            return Response(BAD_REQUEST)
        }
        val currentAuthor = currentAuthorLens(request) ?: return Response(UNAUTHORIZED)
        val permissions = permissionsLens(request)
        if (!permissions.recipeEdit || recipe.authorId != currentAuthor.id)
            return Response(UNAUTHORIZED)
        return Response(OK).with(htmlView(request) of RecipeEditViewModel(
            recipe,
            ingredientListQuery.invoke().withIndex(),
            ingredientListQuery.invoke().size,
        ))
    }
}

class RecipeEditHandler(
    private val currentAuthorLens: RequestContextLens<Author?>,
    private val permissionsLens: RequestContextLens<RolePermissions>,
    private val ingredientListQuery: IngredientListQuery,
    private val recipeFetchQuery: RecipeFetchQuery,
    private val recipeEditQuery: RecipeEditQuery,
    private val htmlView: ContextAwareViewRender,
):HttpHandler {
    companion object {
        private val idLens = Path.uuid().of("id")
        private val nameFormLens = FormField.nonEmptyString().required("name")
        private val recipeDescriptionFormLens = FormField.nonEmptyString().required("recipeDescription")
        private val ingredientListFormLens = FormField.uuid().multi.required("ingredientId")
        private val cookingStepsFormLens = FormField.nonEmptyString().required("cookingSteps")
        private val approximateCookingTimeFormLens = FormField.int().required("approximateCookingTime")
        private val recipeEditFormLens = Body.webForm(
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
        val id = lensOrNull(idLens, request) ?: return Response(BAD_REQUEST)
        val recipe = try {
            recipeFetchQuery(id)
        }catch (_: RecipeFetchException){
            return Response(BAD_REQUEST)
        }
        val currentAuthor = currentAuthorLens(request)!!
        val permissions = permissionsLens(request)
        if (!permissions.recipeEdit || recipe.authorId != currentAuthor.id)
            return Response(UNAUTHORIZED)
        var webForm = recipeEditFormLens(request)
        if (webForm.errors.isEmpty()){
            try {
                recipeEditQuery.invoke(
                    id,
                    currentAuthor.id,
                    nameFormLens(webForm),
                    recipeDescriptionFormLens(webForm),
                    ingredientListFormLens(webForm) as MutableList<UUID>,
                    cookingStepsFormLens(webForm),
                    approximateCookingTimeFormLens(webForm),
                )
                return Response(Status.FOUND).header("location","/cookbook")
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
            }catch (_: RecipeFetchException){
                return Response(BAD_REQUEST)
            }catch (_: AuthorFetchException){
                return Response(BAD_REQUEST)
            }catch (_: IngredientFetchException){
                return Response(BAD_REQUEST)
            }
        }
        return Response(OK).with(htmlView(request) of RecipeEditViewModel(
            recipe,
            ingredientListQuery.invoke().withIndex(),
            ingredientListQuery.invoke().size,
            webForm,
        ))
    }
}
