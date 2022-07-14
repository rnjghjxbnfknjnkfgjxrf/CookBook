package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import java.util.*

class IngredientRepository(
    ingredient : Iterable<Ingredient> = emptyList()
) {
    private val ing  = ingredient.associateBy {it.id}.toMutableMap()

    companion object{
        fun fromJson(node : JsonNode) : IngredientRepository {
            val ing = node.map {
                Ingredient.fromJson(it)
            }
            return IngredientRepository(ing)
        }
    }
    fun asJsonObject(): JsonNode {
        return ing.values
            .map { it.asJsonObject() }
            .asJsonArray()
    }

    fun fetch(id: UUID) : Ingredient? = ing[id]

    fun add(ingredient: Ingredient) : UUID {
        var newId = ingredient.id
        while (ing.containsKey(newId) || newId == EMPTY_UUID){
            newId = UUID.randomUUID()
        }
        ing[newId] = ingredient.setUuid(newId)
        return newId
    }

    fun remove(id: UUID): Ingredient? = ing.remove(id)

    fun list() = ing.values.toList()
}
