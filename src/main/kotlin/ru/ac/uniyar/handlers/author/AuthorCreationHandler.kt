package ru.ac.uniyar.handlers.author

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.FormField
import org.http4k.lens.Invalid
import org.http4k.lens.Validator
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.queries.DescriptionException
import ru.ac.uniyar.domain.queries.EmailAddressException
import ru.ac.uniyar.domain.queries.NickNameException
import ru.ac.uniyar.domain.queries.author.AuthorCreationQuery
import ru.ac.uniyar.handlers.lensOrNull
import ru.ac.uniyar.models.AuthorCreationViewModel
import ru.ac.uniyar.models.template.ContextAwareViewRender

class ShowAuthorCreationForm(
    private val htmlView : ContextAwareViewRender,
    ) : HttpHandler{
    override fun invoke(request: Request): Response {
        return Response(OK).with(htmlView(request) of AuthorCreationViewModel())
    }
}

class AuthorCreationHandler(
    private val authorCreationQuery: AuthorCreationQuery,
    private val htmlView: ContextAwareViewRender,
) : HttpHandler{

    companion object{
        private val nickNameFormLens = FormField.nonEmptyString().required("nickName")
        private val emailAddressFormLens = FormField.nonEmptyString().required("emailAddress")
        private val passwordOneFormLens = FormField.nonEmptyString().required("passwordOne")
        private val passwordTwoFormLens = FormField.nonEmptyString().required("passwordTwo")
        private val descriptionFormLens = FormField.nonEmptyString().required("description")
        private val authorCreationFormLens = Body.webForm(
            Validator.Feedback,
            nickNameFormLens,
            passwordOneFormLens,
            passwordTwoFormLens,
            emailAddressFormLens,
            descriptionFormLens,
        ).toLens()
    }

    override fun invoke(request: Request): Response {
        var webForm = authorCreationFormLens(request)
        val firstPassword = lensOrNull(passwordOneFormLens, webForm)
        val secondPassword = lensOrNull(passwordTwoFormLens, webForm)
        if (firstPassword != secondPassword && firstPassword != null){
            val newErrors = webForm.errors + Invalid(
            passwordOneFormLens.meta.copy(description = "passwords do not match"))
            webForm = webForm.copy(errors = newErrors)
        }
        if (webForm.errors.isEmpty()){
            try {
                authorCreationQuery.invoke(
                    nickNameFormLens(webForm),
                    emailAddressFormLens(webForm),
                    descriptionFormLens(webForm),
                    firstPassword!!,
                )
                return Response(FOUND).header("location", "/authors")
            }catch (_: NickNameException){
                val newErrors = webForm.errors + Invalid(
                    nickNameFormLens.meta.copy(description = "invalid nickname"))
                webForm = webForm.copy(errors = newErrors)
            }catch (_: DescriptionException){
                val newErrors = webForm.errors + Invalid(
                    descriptionFormLens.meta.copy(description = "invalid author description"))
                webForm = webForm.copy(errors = newErrors)
            }catch (_: EmailAddressException){
                val newErrors = webForm.errors + Invalid(
                    emailAddressFormLens.meta.copy(description = "invalid email address format"))
                webForm = webForm.copy(errors = newErrors)
            }

        }
        return Response(OK).with(htmlView(request) of AuthorCreationViewModel(webForm))
    }

}
