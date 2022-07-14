package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import java.nio.file.Path
import kotlin.concurrent.thread
import kotlin.io.path.isReadable
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asPrettyJsonString
import org.http4k.format.Jackson.parse

class Store(
    private val documentStoragePath : Path
) {
    val storeThread = thread(start = false, name = "Store file save"){
        save()
    }
    val recipeRepository : RecipeRepository
    val authorRepository : AuthorRepository
    val ingredientRepository : IngredientRepository
    val rolePermissionsRepository : RolePermissionsRepository

    init{
        val node = if(documentStoragePath.isReadable()){
            val file = documentStoragePath.toFile()
            val jsonDocument = file.readText()
            parse(jsonDocument)
        }else null

        recipeRepository = if (node != null && node.has("recipe")) {
            RecipeRepository.fromJson(node["recipe"])
        }else{
            RecipeRepository()
        }

        authorRepository = if (node != null && node.has("author")){
            AuthorRepository.fromJson(node["author"])
        }else{
            AuthorRepository()
        }

        ingredientRepository = if (node != null && node.has("ingredient")){
            IngredientRepository.fromJson(node["ingredient"])
        }else{
            IngredientRepository()
        }

        rolePermissionsRepository = if (node != null && node.has("rolePermissions")){
            RolePermissionsRepository.fromJson(node["rolePermissions"])
        }else{
            RolePermissionsRepository()
        }
    }

    fun save(){
        val document: JsonNode =
            listOf(
                "recipe" to recipeRepository.asJsonObject(),
                "author" to authorRepository.asJsonObject(),
                "ingredient" to ingredientRepository.asJsonObject(),
                "rolePermissions" to rolePermissionsRepository.asJsonObject(),
            ).asJsonObject()
        documentStoragePath.toFile().writeText(document.asPrettyJsonString())
    }
}
