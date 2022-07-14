package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class Ingredient(
    val id: UUID,
    val name: String,
    val calories: Int,
    val uploadDate: LocalDate = LocalDate.now(),
    val accepted: Boolean = false,
){
    companion object{
        fun fromJson(node: JsonNode) : Ingredient {
            val jsonObject = node.asJsonObject()
            return Ingredient(
                UUID.fromString(jsonObject["id"].asText()),
                jsonObject["name"].asText(),
                jsonObject["calories"].asInt(),
                LocalDate.parse(jsonObject["uploadDate"].asText(), DateTimeFormatter.ISO_DATE),
                jsonObject["accepted"].asBoolean(),
            )
        }
    }

    fun asJsonObject() : JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "name" to name.asJsonValue(),
            "calories" to calories.asJsonValue(),
            "uploadDate" to uploadDate.format(DateTimeFormatter.ISO_DATE).asJsonValue(),
            "accepted" to accepted.asJsonValue(),
        ).asJsonObject()
    }

    fun setUuid(uuid: UUID) : Ingredient {
        return this.copy(id = uuid)
    }
}
