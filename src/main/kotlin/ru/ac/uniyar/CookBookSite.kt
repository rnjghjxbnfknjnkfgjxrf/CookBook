package ru.ac.uniyar

import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Undertow
import org.http4k.server.asServer
import ru.ac.uniyar.domain.StoreHolder
import ru.ac.uniyar.domain.storage.Author
import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.SettingsFileException
import ru.ac.uniyar.filters.JwtTools
import ru.ac.uniyar.filters.authenticationFilter
import ru.ac.uniyar.filters.authorizationFilter
import ru.ac.uniyar.filters.showErrorMessageFilter
import ru.ac.uniyar.handlers.HttpHandlersHolder
import ru.ac.uniyar.models.createRouter
import ru.ac.uniyar.models.template.ContextAwarePebbleTemplates
import ru.ac.uniyar.models.template.ContextAwareViewRender
import kotlin.io.path.Path

const val SERVER_PORT = 9000

fun main() {
    val storeHolder = try {
        StoreHolder(Path("storage.json"), Path("settings.json"))
    } catch (exc: SettingsFileException) {
        println(exc.message)
        return
    }
    Runtime.getRuntime().addShutdownHook(storeHolder.store.storeThread)

    val renderer = ContextAwarePebbleTemplates().hotReload("src/main/resources")
    val htmlView = ContextAwareViewRender(renderer, ContentType.TEXT_HTML)

    val contexts = RequestContexts()
    val currentAuthorLens: RequestContextLens<Author?> = RequestContextKey.optional(contexts, "author")
    val permissionsLens: RequestContextLens<RolePermissions> =
        RequestContextKey.required(contexts, name = "permissions")
    htmlView.associateContextLens("currentAuthor", currentAuthorLens)
    htmlView.associateContextLens("permissions", permissionsLens)

    val jwtTools = JwtTools(storeHolder.settings.salt, issuer = "ru.ac.uniyar.CookBookSite")

    val handlerHolder = HttpHandlersHolder(
        currentAuthorLens,
        permissionsLens,
        htmlView,
        storeHolder,
        jwtTools,
    )

    val router = createRouter(handlerHolder)

    val authorisedApp = authenticationFilter(
        currentAuthorLens,
        storeHolder.fetchAuthorViaToken,
        jwtTools
    ).then(
        authorizationFilter(
            currentAuthorLens,
            permissionsLens,
            storeHolder.fetchPermissionsViaToken
        )
    ).then(
        router.invoke()
    )
    val staticFilesHandler = static(ResourceLoader.Classpath("/ru/ac/uniyar/public/"))
    val app = routes(
        authorisedApp,
        staticFilesHandler
    )
    val printingApp: HttpHandler =
        ServerFilters.InitialiseRequestContext(contexts)
            .then(showErrorMessageFilter(htmlView))
            .then(app)
    val server = printingApp.asServer(Undertow(SERVER_PORT)).start()
    println("Server started on http://localhost:" + server.port())
}
