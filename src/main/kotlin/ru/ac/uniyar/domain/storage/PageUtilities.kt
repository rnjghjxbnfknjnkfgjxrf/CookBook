package ru.ac.uniyar.domain.storage


fun countPageNumbers(size : Int, pageSize : Int) = (size + pageSize - 1)/pageSize

data class PagedResult<T>(val values: List<T>, val pageCount : Int)

fun <E> List<E>.subListOrEmpty(from : Int, to : Int): List<E>{
    val end = if (to > this.size) this.size else to
    if (from < 0 || from >= end){
        return emptyList()
    }
    return this.subList(from, end)
}
