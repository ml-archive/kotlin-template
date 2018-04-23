package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by Christian on 4/20/2018.
 * @author   Christian
 * @since    4/20/2018.
 */
class PostAuthenticateUserInteractorImpl(executor: Executor, val api: Api) : BaseInteractor(executor), PostAuthenticateUserInteractor {
    override var input: PostAuthenticateUserInteractor.Input? = null
    override var output: PostAuthenticateUserInteractor.Output? = null

    override fun execute() {

        try {
            input?.let {
                val map = mapOf(
                        Pair("grant_type", "password"),
                        Pair("username", "nodes-user1"), // TODO: what to use as username for login?
                        Pair("password", it.password),
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_Id", "simplelogin"),
                        Pair("secret", "2BB80D537B1DA3E38BD30361AA855686BDE0EACD7162FEF6A25FE97BF527A25B") // TODO: what's this?
                )
                it.activationCode?.let {
                    map.plus(Pair("acr_values", "activationcode:$it"))
                }

                val result = api.postLogin(map).execute()
                runOnUIThread {
                    if (result.isSuccessful) {
                        result?.body()?.let {
                            output?.onAuthenticationsSuccess(input!!.user, it)
                        }
                    } else {
                        output?.onAuthenticationsDenied(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onAuthenticationsError(exceptionToViewError(t))
            }
        }
    }
}