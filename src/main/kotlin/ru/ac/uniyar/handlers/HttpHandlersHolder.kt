package ru.ac.uniyar.handlers

import org.http4k.core.Request
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.StoreHolder
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.filters.JwtTools
import ru.ac.uniyar.handlers.author.AuthorCreationHandler
import ru.ac.uniyar.handlers.author.AuthorsHandler
import ru.ac.uniyar.handlers.author.ChosenAuthorHandler
import ru.ac.uniyar.handlers.author.ShowAuthorCreationForm
import ru.ac.uniyar.handlers.ingredient.ChosenIngredientHandler
import ru.ac.uniyar.handlers.ingredient.IngredientAcceptHandler
import ru.ac.uniyar.handlers.ingredient.IngredientCreationHandler
import ru.ac.uniyar.handlers.ingredient.IngredientDeleteHandler
import ru.ac.uniyar.handlers.ingredient.IngredientEditHandler
import ru.ac.uniyar.handlers.ingredient.IngredientsHandler
import ru.ac.uniyar.handlers.ingredient.ShowIngredientAcceptForm
import ru.ac.uniyar.handlers.ingredient.ShowIngredientCreationForm
import ru.ac.uniyar.handlers.ingredient.ShowIngredientDeleteForm
import ru.ac.uniyar.handlers.ingredient.ShowIngredientEditForm
import ru.ac.uniyar.handlers.recipe.ChosenRecipeHandler
import ru.ac.uniyar.handlers.recipe.RecipeCreationHandler
import ru.ac.uniyar.handlers.recipe.RecipeChangeVisibilityHandler
import ru.ac.uniyar.handlers.recipe.RecipeDeleteHandler
import ru.ac.uniyar.handlers.recipe.RecipeEditHandler
import ru.ac.uniyar.handlers.recipe.ShowRecipeCreationForm
import ru.ac.uniyar.handlers.recipe.ShowRecipeChangeVisibilityForm
import ru.ac.uniyar.handlers.recipe.ShowRecipeDeleteForm
import ru.ac.uniyar.handlers.recipe.ShowRecipeEditForm
import ru.ac.uniyar.models.template.ContextAwareViewRender

class HttpHandlersHolder(
    currentAuthorLens: BiDiLens<Request, Author?>,
    permissionsLens: BiDiLens<Request, RolePermissions>,
    htmlView: ContextAwareViewRender,
    storeHolder: StoreHolder,
    jwtTools: JwtTools,
) {
    val showAuthorCreationForm = ShowAuthorCreationForm(
        htmlView,
    )
    val authorCreationHandler = AuthorCreationHandler(
        storeHolder.authorCreationQuery,
        htmlView,
    )
    val authorsHandler = AuthorsHandler(
        storeHolder.authorsFilterQuery,
        storeHolder.authorsQuery,
        htmlView,
    )
    val chosenAuthorHandler = ChosenAuthorHandler(
        storeHolder.authorFetchQuery,
        htmlView,
    )

    val chosenIngredientHandler = ChosenIngredientHandler(
        storeHolder.ingredientInfoFetchQuery,
        htmlView,
        permissionsLens,
    )
    val showIngredientCreationForm = ShowIngredientCreationForm(
        permissionsLens,
        htmlView)
    val ingredientCreationHandler = IngredientCreationHandler(
        permissionsLens,
        storeHolder.ingredientCreationQuery,
        htmlView,
    )
    val ingredientsHandler = IngredientsHandler(
        storeHolder.ingredientsFilterQuery,
        storeHolder.ingredientsQuery,
        htmlView,
        permissionsLens,
    )
    val showIngredientAcceptForm = ShowIngredientAcceptForm(
        storeHolder.ingredientFetchQuery,
        permissionsLens,
        htmlView,
    )
    val ingredientAcceptHandler = IngredientAcceptHandler(
        storeHolder.ingredientFetchQuery,
        storeHolder.ingredientAcceptQuery,
        htmlView,
        permissionsLens,
    )
    val showIngredientDeleteForm = ShowIngredientDeleteForm(
        permissionsLens,
        storeHolder.ingredientFetchQuery,
        htmlView,
    )
    val ingredientDeleteHandler = IngredientDeleteHandler(
        permissionsLens,
        storeHolder.ingredientDeleteQuery,
    )
    val showIngredientEditForm = ShowIngredientEditForm(
        storeHolder.ingredientFetchQuery,
        permissionsLens,
        htmlView,
    )
    val ingredientEditHandler = IngredientEditHandler(
        storeHolder.ingredientFetchQuery,
        storeHolder.ingredientEditQuery,
        permissionsLens,
        htmlView,
    )

    val chosenRecipeHandler = ChosenRecipeHandler(
        storeHolder.recipeInfoFetchQuery,
        htmlView,
    )
    val showRecipeCreationForm = ShowRecipeCreationForm(
        permissionsLens,
        storeHolder.ingredientListQuery,
        htmlView,
    )
    val recipeCreationHandler = RecipeCreationHandler(
        currentAuthorLens,
        permissionsLens,
        storeHolder.ingredientListQuery,
        storeHolder.recipeCreationQuery,
        htmlView,
    )
    val cookBookHandler = CookBookHandler(
        storeHolder.cookBookFilterQuery,
        storeHolder.cookBookQuery,
        htmlView,
        currentAuthorLens,
        permissionsLens,
    )
    val showRecipeChangeVisibilityForm = ShowRecipeChangeVisibilityForm(
        storeHolder.recipeFetchQuery,
        permissionsLens,
        htmlView,
    )
    val recipeChangeVisibilityHandler = RecipeChangeVisibilityHandler(
        storeHolder.recipeChangeVisibilityQuery,
        permissionsLens,
    )
    val showRecipeEditForm = ShowRecipeEditForm(
        currentAuthorLens,
        permissionsLens,
        storeHolder.ingredientListQuery,
        storeHolder.recipeFetchQuery,
        htmlView,
    )
    val recipeEditHandler = RecipeEditHandler(
        currentAuthorLens,
        permissionsLens,
        storeHolder.ingredientListQuery,
        storeHolder.recipeFetchQuery,
        storeHolder.recipeEditQuery,
        htmlView,
    )
    val showRecipeDeleteForm = ShowRecipeDeleteForm(
        currentAuthorLens,
        storeHolder.recipeFetchQuery,
        htmlView,
    )
    val recipeDeleteHandler = RecipeDeleteHandler(
        currentAuthorLens,
        storeHolder.recipeFetchQuery,
        storeHolder.recipeDeleteQuery,
    )

    val mainPageHandler = MainPageHandler(
        storeHolder.mainPageQuery,
        htmlView,
    )

    val showLoginFormHandler = ShowLoginFormHandler(htmlView)
    val authenticateUser = AuthenticateUser(
        storeHolder.authenticateUserViaLoginQuery,
        htmlView,
        jwtTools,
    )
}
