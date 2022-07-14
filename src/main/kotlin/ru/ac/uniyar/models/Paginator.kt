package ru.ac.uniyar.models

import org.http4k.core.Uri

class Paginator(
    val pageCount: Int,
    val currentPage: Int,
    val pageUri: Uri,
) {
    fun canNavigate(): Boolean = pageCount > 1

    fun canGoBackWard() : Boolean = currentPage > 1

    fun canGoForward() : Boolean = currentPage < pageCount

    fun previousPageLink() :  Paginator = Paginator(
        pageCount,
        currentPage - 1,
        Uri.of(pageUri.path+"?page=${currentPage-1}"))

    fun previousPageLinks() : List<Paginator>{
        val prevPageLinks = mutableListOf<Paginator>()
        for (i in 1..currentPage-1) prevPageLinks.add(Paginator(
            pageCount,
            i,
            Uri.of(pageUri.path+"?page=${i}")
        ))
        return prevPageLinks
    }

    fun nextPageLink() :  Paginator = Paginator(
        pageCount,
        currentPage + 1,
        Uri.of(pageUri.path+"?page=${currentPage+1}"))

    fun nextPageLinks() : List<Paginator>{
        val nexPageLinks = mutableListOf<Paginator>()
        for (i in currentPage..pageCount-1) nexPageLinks.add(Paginator(
            pageCount,
            i+1,
            Uri.of(pageUri.path+"?page=${i+1}")
        ))
        return nexPageLinks
    }


}
