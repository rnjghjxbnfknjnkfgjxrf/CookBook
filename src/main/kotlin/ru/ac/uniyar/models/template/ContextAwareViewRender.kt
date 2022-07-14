package ru.ac.uniyar.models.template

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.RequestContextLens
import org.http4k.lens.string
import org.http4k.template.ViewModel
import ru.ac.uniyar.handlers.lensOrNull

class ContextAwareViewRender(
    private val templateRenderer: ContextAwareTemplateRenderer,
    contentType: ContentType,
) {
    private val baseBodyLensSpec = Body.string(contentType)
    private val contexts = mutableMapOf<String, RequestContextLens<*>>()

    fun associateContextLens(key: String, lens: RequestContextLens<*>){
        contexts[key] = lens
    }

    /*private fun extractContext(request: Request): Map<String, Any?> = contexts.mapValues {
        it.value(request)
    }*/
    private fun extractContext(request: Request): Map<String, Any?> = contexts.mapValues {
        lensOrNull(it.value, request)
    }
    operator fun invoke(request: Request): BiDiBodyLens<ViewModel> {
        return baseBodyLensSpec.map<ViewModel>(
            {
                throw UnsupportedOperationException("Cannot parse a ViewModel")
            },
            { viewModel: ViewModel ->
                templateRenderer(extractContext(request), viewModel)
            }
        ).toLens()
    }
}
