package ru.ac.uniyar.domain.storage

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson.asJsonArray
import java.util.*

class AuthorRepository(
    author : Iterable<Author> = emptyList()
) {
    private val authors = author.associateBy { it.id }.toMutableMap()

    companion object{
        fun fromJson(node : JsonNode) : AuthorRepository {
            val authors = node.map {
                Author.fromJson(it)
            }
            return AuthorRepository(authors)
        }
    }

    fun asJsonObject(): JsonNode{
        return authors.values
            .map { it.asJsonObject() }
            .asJsonArray()
    }

    fun fetch(id: UUID) : Author? = authors[id]

    fun add(author : Author) : UUID {
        var newId = author.id
        while (authors.containsKey(newId) || newId == EMPTY_UUID){
            newId = UUID.randomUUID()
        }
        authors[newId] = author.setUuid(newId)
        return newId
    }

    fun list() = authors.values.toList()

}
