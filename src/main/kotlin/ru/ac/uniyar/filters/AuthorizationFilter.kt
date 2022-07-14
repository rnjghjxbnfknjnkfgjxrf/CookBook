package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.queries.FetchPermissionsViaToken
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.RolePermissions

fun authorizationFilter(
    currentAuthorLens: RequestContextLens<Author?>,
    permissionsLens: RequestContextLens<RolePermissions>,
    fetchPermissionsViaToken: FetchPermissionsViaToken,
): Filter = Filter {next: HttpHandler ->
    { request: Request ->  
        val permissions = currentAuthorLens(request)?.let {
            fetchPermissionsViaToken(it.roleId)
        } ?: RolePermissions.GUEST_ROLE
        val authorizedRequest = request.with(permissionsLens of permissions)
        next(authorizedRequest)
    }
}
