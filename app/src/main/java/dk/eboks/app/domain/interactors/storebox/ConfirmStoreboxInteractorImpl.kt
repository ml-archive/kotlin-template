package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by Christian on 5/15/2018.
 * @author Christian
 * @since 5/15/2018.
 */
class ConfirmStoreboxInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), ConfirmStoreboxInteractor {
    override var input: ConfirmStoreboxInteractor.Input? = null
    override var output: ConfirmStoreboxInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val map = mapOf(
                    Pair("id", it.id),
                    Pair("code", it.code)
                )

                val result = api.postActivateStorebox(map).execute()
                runOnUIThread {
                    output?.onLinkingSuccess(result.isSuccessful)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            runOnUIThread {
                output?.onError(exceptionToViewError(t))
            }
        }
    }
}