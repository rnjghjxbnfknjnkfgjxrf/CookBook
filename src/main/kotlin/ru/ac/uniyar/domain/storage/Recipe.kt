package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import java.util.*
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Recipe (
    val id : UUID,
    val authorId : UUID,
    val name: String,
    val recipeDescription : String,
    val ingredientList : MutableList<UUID>,
    val cookingSteps : String,
    val approximateCookingTime : Int,
    val uploadDate: LocalDate = LocalDate.now(),
    val hidden: Boolean = false,
){

    companion object{
        fun fromJson(node: JsonNode) : Recipe {
            val jsonObject = node.asJsonObject()
            return Recipe(
                UUID.fromString(jsonObject["id"].asText()),
                UUID.fromString(jsonObject["author_id"].asText()),
                jsonObject["name"].asText(),
                jsonObject["recipeDescription"].asText(),
                jsonObject["ingredientList"].map{ UUID.fromString (it.asText()) } as MutableList<UUID>,
                jsonObject["cookingSteps"].asText(),
                jsonObject["approximateCookingTime"].asInt(),
                LocalDate.parse(jsonObject["uploadDate"].asText(), DateTimeFormatter.ISO_DATE),
                jsonObject["hidden"].asBoolean(),
            )
        }
    }

    fun asJsonObject() : JsonNode{
        return listOf(
            "id" to id.toString().asJsonValue(),
            "author_id" to authorId.toString().asJsonValue(),
            "name" to name.asJsonValue(),
            "recipeDescription" to recipeDescription.asJsonValue(),
            "ingredientList" to ingredientList.asJsonObject(),
            "cookingSteps" to cookingSteps.asJsonValue(),
            "approximateCookingTime" to approximateCookingTime.asJsonValue(),
            "uploadDate" to uploadDate.format(DateTimeFormatter.ISO_DATE).asJsonValue(),
            "hidden" to hidden.asJsonValue(),
        ).asJsonObject()
    }

    fun setUuid(uuid: UUID) : Recipe {
        return this.copy(id = uuid)
    }
}
