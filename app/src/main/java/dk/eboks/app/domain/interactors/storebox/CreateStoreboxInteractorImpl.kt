package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by Christian on 5/15/2018.
 * @author Christian
 * @since 5/15/2018.
 */
class CreateStoreboxInteractorImpl(executor: Executor, private val api: Api) :
    BaseInteractor(executor), CreateStoreboxInteractor {
    override var output: CreateStoreboxInteractor.Output? = null

    override fun execute() {
        try {
            val result = api.createStoreboxAccount().execute()
            if (result.isSuccessful) {
                runOnUIThread {
                    output?.onStoreboxAccountCreated()
                }
            } else {
                runOnUIThread {
                    output?.onStoreboxAccountCreatedError(ViewError())
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                Timber.e(t)
                if (t is ServerErrorException) {
                    val error = t.error
                    Timber.e("got servererroreception")
                    when (error.code) {
                        // TODO fix to use the correct error code when API returns it
                        // APIConstants.STOREBOX_PROFILE_EXISTS -> {
                        0 -> {
                            output?.onStoreboxAccountExists()
                        }
                        else -> {
                            output?.onStoreboxAccountCreatedError(exceptionToViewError(t))
                        }
                    }
                } else
                    output?.onStoreboxAccountCreatedError(exceptionToViewError(t))
            }
        }
    }
}