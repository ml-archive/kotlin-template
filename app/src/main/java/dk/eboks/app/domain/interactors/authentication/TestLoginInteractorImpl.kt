package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.AuthException
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class TestLoginInteractorImpl(
        executor: Executor,
        val appStateManager: AppStateManager,
        val authClient: AuthClient) : BaseInteractor(executor), TestLoginInteractor {
    override var output: TestLoginInteractor.Output? = null
    override var input: TestLoginInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                try {
                    if(!args.activationCode.isNullOrBlank()) {
                        authClient.login(
                                username = args.username,
                                password = args.password,
                                longClient = false,
                                verifyOnly = true
                        )
                    }
                    else
                    {
                        authClient.login(
                                username = args.username,
                                password = args.password,
                                longClient = false,
                                verifyOnly = true
                        )
                    }


                    runOnUIThread {
                        output?.onTestLoginSuccess()
                    }
                }
                catch (e : AuthException)
                {
                    Timber.e("AuthException = ${e.httpCode} ${e.errorDescription}")
                    if(e.httpCode == 400)
                    {
                        runOnUIThread {
                            when {
                                e.errorDescription.contentEquals("User verification error") -> output?.onTestLoginDenied(ViewError(
                                        title = Translation.logoncredentials.invalidCredentialsTitle,
                                        message = Translation.logoncredentials.invalidCredentialsMessage,
                                        shouldCloseView = false))
                                e.errorDescription.contentEquals("Activation code is required") -> output?.onTestLoginSuccess() // counts as a success, we won't bother actually getting the activationCode
                                else -> output?.onTestLoginDenied(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = false))
                            }
                        }
                    }
                    else
                    {
                        runOnUIThread {
                            output?.onTestLoginDenied(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true))
                        }
                    }
                }

            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onTestLoginError(exceptionToViewError(t))
            }
        }
    }
}