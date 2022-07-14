package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonObject
import org.http4k.format.Jackson.asJsonValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class Author(
    val id: UUID,
    val roleId: UUID,
    val nickName: String,
    val emailAddress: String,
    val description: String,
    val password: String,
    val uploadDate: LocalDate = LocalDate.now(),
) {
    companion object{
        fun fromJson(node: JsonNode) : Author {
            val jsonObject = node.asJsonObject()
            return Author(
                UUID.fromString(jsonObject["id"].asText()),
                UUID.fromString(jsonObject["roleId"].asText()),
                jsonObject["nickName"].asText(),
                jsonObject["emailAddress"].asText(),
                jsonObject["description"].asText(),
                jsonObject["password"].asText(),
                LocalDate.parse(jsonObject["uploadDate"].asText(), DateTimeFormatter.ISO_DATE),
            )
        }
    }

    fun asJsonObject() : JsonNode {
        return listOf(
            "id" to id.toString().asJsonValue(),
            "roleId" to roleId.toString().asJsonValue(),
            "nickName" to nickName.asJsonValue(),
            "emailAddress" to emailAddress.asJsonValue(),
            "description" to description.asJsonValue(),
            "password" to password.asJsonValue(),
            "uploadDate" to uploadDate.format(DateTimeFormatter.ISO_DATE).asJsonValue(),
        ).asJsonObject()
    }

    fun setUuid(uuid: UUID) : Author {
        return this.copy(id = uuid)
    }
}
