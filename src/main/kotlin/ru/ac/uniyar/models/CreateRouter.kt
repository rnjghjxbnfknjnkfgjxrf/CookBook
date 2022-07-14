package ru.ac.uniyar.models

import ru.ac.uniyar.Router
import ru.ac.uniyar.handlers.HttpHandlersHolder

fun createRouter(handlerHolder: HttpHandlersHolder): Router {
    return Router(handlerHolder.cookBookHandler,
        handlerHolder.showLoginFormHandler,
        handlerHolder.authenticateUser,
        handlerHolder.showRecipeCreationForm,
        handlerHolder.recipeCreationHandler,
        handlerHolder.chosenRecipeHandler,
        handlerHolder.authorsHandler,
        handlerHolder.showAuthorCreationForm,
        handlerHolder.authorCreationHandler,
        handlerHolder.chosenAuthorHandler,
        handlerHolder.ingredientsHandler,
        handlerHolder.showIngredientCreationForm,
        handlerHolder.ingredientCreationHandler,
        handlerHolder.chosenIngredientHandler,
        handlerHolder.mainPageHandler,
        handlerHolder.ingredientAcceptHandler,
        handlerHolder.showIngredientAcceptForm,
        handlerHolder.showIngredientDeleteForm,
        handlerHolder.ingredientDeleteHandler,
        handlerHolder.showIngredientEditForm,
        handlerHolder.ingredientEditHandler,
        handlerHolder.showRecipeChangeVisibilityForm,
        handlerHolder.recipeChangeVisibilityHandler,
        handlerHolder.showRecipeEditForm,
        handlerHolder.recipeEditHandler,
        handlerHolder.showRecipeDeleteForm,
        handlerHolder.recipeDeleteHandler,
    )
}
