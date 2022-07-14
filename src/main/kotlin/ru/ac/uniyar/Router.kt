package ru.ac.uniyar

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import ru.ac.uniyar.handlers.LogOutUser

@Suppress("LongParameterList")
class Router(
    private val cookBookHandler: HttpHandler,
    private val showLoginFormHandler: HttpHandler,
    private val authenticateUser: HttpHandler,
    private val showRecipeCreationForm: HttpHandler,
    private val recipeCreationHandler: HttpHandler,
    private val chosenRecipeHandler: HttpHandler,
    private val authorsHandler: HttpHandler,
    private val showAuthorCreationForm: HttpHandler,
    private val authorCreationHandler: HttpHandler,
    private val chosenAuthorHandler: HttpHandler,
    private val ingredientsHandler: HttpHandler,
    private val showIngredientCreationForm: HttpHandler,
    private val ingredientCreationHandler: HttpHandler,
    private val chosenIngredientHandler: HttpHandler,
    private val mainPageHandler: HttpHandler,
    private val ingredientAcceptHandler: HttpHandler,
    private val showIngredientAcceptForm: HttpHandler,
    private val showIngredientDeleteForm: HttpHandler,
    private val ingredientDeleteHandler: HttpHandler,
    private val showIngredientEditForm: HttpHandler,
    private val ingredientEditHandler: HttpHandler,
    private val showRecipeHideForm: HttpHandler,
    private val recipeHideHandler: HttpHandler,
    private val showRecipeEditForm: HttpHandler,
    private val recipeEditHandler: HttpHandler,
    private val showRecipeDeleteForm: HttpHandler,
    private val recipeDeleteHandler: HttpHandler,
) {
    operator fun invoke(): RoutingHttpHandler = routes(
        "/cookbook" bind GET to cookBookHandler,
        "/login" bind GET to showLoginFormHandler,
        "/login" bind POST to authenticateUser,
        "/logout" bind GET to LogOutUser(),
        "/cookbook/new" bind GET to showRecipeCreationForm,
        "/cookbook/new" bind POST to recipeCreationHandler,
        "/cookbook/{id}" bind GET to chosenRecipeHandler,
        "/cookbook/{id}/change_visibility" bind GET to showRecipeHideForm,
        "/cookbook/{id}/change_visibility" bind POST to recipeHideHandler,
        "/cookbook/{id}/edit" bind GET to showRecipeEditForm,
        "/cookbook/{id}/edit" bind POST to recipeEditHandler,
        "/cookbook/{id}/delete" bind GET to showRecipeDeleteForm,
        "/cookbook/{id}/delete" bind POST to recipeDeleteHandler,
        "/authors" bind GET to authorsHandler,
        "/authors/new" bind GET to showAuthorCreationForm,
        "/authors/new" bind POST to authorCreationHandler,
        "/authors/{id}" bind GET to chosenAuthorHandler,
        "/ingredients" bind GET to ingredientsHandler,
        "/ingredients/accept/{id}" bind GET to showIngredientAcceptForm,
        "/ingredients/accept/{id}" bind POST to ingredientAcceptHandler,
        "/ingredients/new" bind GET to showIngredientCreationForm,
        "/ingredients/new" bind POST to ingredientCreationHandler,
        "/ingredients/{id}" bind GET to chosenIngredientHandler,
        "/ingredients/{id}/delete" bind GET to showIngredientDeleteForm,
        "/ingredients/{id}/delete" bind POST to ingredientDeleteHandler,
        "/ingredients/{id}/edit" bind GET to showIngredientEditForm,
        "/ingredients/{id}/edit" bind POST to ingredientEditHandler,
        "/" bind GET to mainPageHandler,
    )
}
