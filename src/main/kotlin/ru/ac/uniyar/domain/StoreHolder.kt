package ru.ac.uniyar.domain

import ru.ac.uniyar.domain.queries.AuthenticateUserViaLoginQuery
import ru.ac.uniyar.domain.queries.CookBookFilterQuery
import ru.ac.uniyar.domain.queries.CookBookListQuery
import ru.ac.uniyar.domain.queries.CookBookQuery
import ru.ac.uniyar.domain.queries.FetchAuthorViaToken
import ru.ac.uniyar.domain.queries.FetchPermissionsViaToken
import ru.ac.uniyar.domain.queries.MainPageQuery
import ru.ac.uniyar.domain.queries.author.AuthorCreationQuery
import ru.ac.uniyar.domain.queries.author.AuthorFetchQuery
import ru.ac.uniyar.domain.queries.author.AuthorsFilterQuery
import ru.ac.uniyar.domain.queries.author.AuthorsQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientAcceptQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientCreationQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientDeleteQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientEditQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientFetchQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientInfoFetchQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientListQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientsFilterQuery
import ru.ac.uniyar.domain.queries.ingredient.IngredientsQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeCreationQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeFetchQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeChangeVisibilityQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeDeleteQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeEditQuery
import ru.ac.uniyar.domain.queries.recipe.RecipeInfoFetchQuery
import ru.ac.uniyar.domain.storage.Settings
import ru.ac.uniyar.domain.storage.Store
import java.nio.file.Path

class StoreHolder(
    documentStorePath: Path,
    settingsPath: Path,
) {
    val settings = Settings(settingsPath)
    val store = Store(documentStorePath)

    val authorCreationQuery = AuthorCreationQuery(
        store.authorRepository,
        settings
    )
    val authorFetchQuery = AuthorFetchQuery(
        store.authorRepository,
        store.recipeRepository
    )
    val authorsFilterQuery = AuthorsFilterQuery(
        store.authorRepository,
        store.recipeRepository
    )
    val authorsQuery = AuthorsQuery(
        store.authorRepository,
        store.recipeRepository
    )

    val ingredientCreationQuery = IngredientCreationQuery(store.ingredientRepository)
    val ingredientInfoFetchQuery = IngredientInfoFetchQuery(
        store.ingredientRepository,
        store.recipeRepository
    )
    val ingredientListQuery = IngredientListQuery(store.ingredientRepository)
    val ingredientsFilterQuery = IngredientsFilterQuery(store.ingredientRepository)
    val ingredientsQuery = IngredientsQuery(store.ingredientRepository)
    val ingredientFetchQuery = IngredientFetchQuery(store.ingredientRepository)
    val ingredientAcceptQuery = IngredientAcceptQuery(store.ingredientRepository)
    val ingredientDeleteQuery = IngredientDeleteQuery(
        store.ingredientRepository,
        store.recipeRepository
    )
    val ingredientEditQuery = IngredientEditQuery(store.ingredientRepository)

    val recipeCreationQuery = RecipeCreationQuery(
        store.recipeRepository,
        store.authorRepository,
        store.ingredientRepository
    )
    val recipeInfoFetchQuery = RecipeInfoFetchQuery(
        store.recipeRepository, store.authorRepository, store.ingredientRepository)
    val cookBookFilterQuery = CookBookFilterQuery(store.recipeRepository)
    val cookBookListQuery = CookBookListQuery(store.recipeRepository)
    val cookBookQuery = CookBookQuery(
        store.recipeRepository,
        store.authorRepository
    )
    val recipeChangeVisibilityQuery = RecipeChangeVisibilityQuery(store.recipeRepository)
    val recipeFetchQuery = RecipeFetchQuery(store.recipeRepository)
    val recipeEditQuery = RecipeEditQuery(
        store.recipeRepository,
        store.authorRepository,
        store.ingredientRepository,
    )
    val recipeDeleteQuery = RecipeDeleteQuery(store.recipeRepository)

    val mainPageQuery = MainPageQuery(
        store.recipeRepository,
        store.ingredientRepository
    )

    val authenticateUserViaLoginQuery = AuthenticateUserViaLoginQuery(
        store.authorRepository,
        settings
    )
    val fetchAuthorViaToken = FetchAuthorViaToken(store.authorRepository)
    val fetchPermissionsViaToken = FetchPermissionsViaToken(store.rolePermissionsRepository)
}
