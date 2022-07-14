package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import java.util.UUID

class RecipeRepository(
    recipe : Iterable<Recipe> = emptyList()
) {
    private val rec  = recipe.associateBy {it.id}.toMutableMap()

    companion object{
        fun fromJson(node : JsonNode) : RecipeRepository {
            val rec = node.map {
                Recipe.fromJson(it)
            }
            return RecipeRepository(rec)
        }
    }
    fun asJsonObject(): JsonNode{
        return rec.values
            .map { it.asJsonObject() }
            .asJsonArray()
    }

    fun fetch(id: UUID) : Recipe? = rec[id]

    fun add(recipe : Recipe) : UUID{
        var newId = recipe.id
        while (rec.containsKey(newId) || newId == EMPTY_UUID){
            newId = UUID.randomUUID()
        }
        rec[newId] = recipe.setUuid(newId)
        return newId
    }

    fun remove(id: UUID): Recipe? = rec.remove(id)

    fun list() = rec.values.toList()

}
