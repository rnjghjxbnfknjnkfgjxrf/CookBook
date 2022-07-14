package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.template.TemplateRenderer
import ru.ac.uniyar.models.LoginOfferViewModel
import ru.ac.uniyar.models.ShowErrorInfoViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

const val UNAUTHORISED_CODE = 401

fun showErrorMessageFilter(renderer: ContextAwareViewRender): Filter = Filter{ next : HttpHandler ->
    { request ->
        val response = next(request)
        if (response.status.successful){
            response
        }else if(response.status.code == UNAUTHORISED_CODE){
            response.with(renderer(request) of LoginOfferViewModel(""))
        }else{
            //response.body(renderer(ShowErrorInfoViewModel(request.uri)))
            response.with(renderer(request) of ShowErrorInfoViewModel(request.uri))
        }
    }
}
