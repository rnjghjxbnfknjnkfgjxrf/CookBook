package ru.ac.uniyar.domain.storage


import com.fasterxml.jackson.databind.JsonNode
import org.http4k.contract.openapi.OpenAPIJackson.asJsonObject
import org.http4k.contract.openapi.OpenAPIJackson.asJsonValue
import java.util.UUID

data class RolePermissions(
    val id : UUID,
    val name: String,
    val recipeCreation: Boolean,
    val recipeEdit: Boolean,
    val ingredientCreation: Boolean,
    val ingredientAccept: Boolean,
    val ingredientEdit: Boolean,
    val recipeHide: Boolean,
){
    companion object{
        fun fromJson(jsonNode: JsonNode): RolePermissions{
            val jsonObject = jsonNode.asJsonObject()
            return RolePermissions(
                UUID.fromString(jsonObject["id"].asText()),
                jsonObject["name"].asText(),
                jsonObject["recipeCreation"].asBoolean(),
                jsonObject["recipeEdit"].asBoolean(),
                jsonObject["ingredientCreation"].asBoolean(),
                jsonObject["ingredientAccept"].asBoolean(),
                jsonObject["ingredientEdit"].asBoolean(),
                jsonObject["recipeHide"].asBoolean(),
            )
        }

        val GUEST_ROLE = RolePermissions(
            id = UUID.fromString("39173158-db6b-11ec-9d64-0242ac120002"),
            name = "Гость",
            recipeCreation = false,
            recipeEdit =  false,
            ingredientCreation = false,
            ingredientAccept = false,
            ingredientEdit = false,
            recipeHide = false,
        )
    }

    fun asJsonObject(): JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "name" to name.asJsonValue(),
            "recipeCreation" to recipeCreation.asJsonValue(),
            "recipeEdit" to recipeEdit.asJsonValue(),
            "ingredientCreation" to ingredientCreation.asJsonValue(),
            "ingredientAccept" to ingredientAccept.asJsonValue(),
            "ingredientEdit" to ingredientEdit.asJsonValue(),
            "recipeHide" to recipeHide.asJsonValue(),
        ).asJsonObject()
    }
}
