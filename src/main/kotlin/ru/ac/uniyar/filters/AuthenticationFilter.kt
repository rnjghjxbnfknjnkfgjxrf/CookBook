package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.queries.FetchAuthorViaToken
import ru.ac.uniyar.domain.storage.Author

fun authenticationFilter(
    currentAuthor: BiDiLens<Request, Author?>,
    fetchAuthorViaToken: FetchAuthorViaToken,
    jwtTools: JwtTools,
): Filter = Filter { next: HttpHandler ->
    { request: Request ->
        val requestWithAuthor = request.cookie("token")?.value  ?.let { token ->
            jwtTools.subject(token)
        }?.let { userId ->
            fetchAuthorViaToken(userId)
        }?.let { author ->
            request.with( currentAuthor of author)
        } ?: request
        next(requestWithAuthor)
    }
}
