package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by Christian on 5/15/2018.
 * @author   Christian
 * @since    5/15/2018.
 */
class LinkStoreboxInteractorImpl(executor: Executor, private val api: Api) : BaseInteractor(executor), LinkStoreboxInteractor {
    override var input: LinkStoreboxInteractor.Input? = null
    override var output: LinkStoreboxInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                val map = mapOf(
                        Pair("email", it.email),
                        Pair("mobile", it.mobile)
                )

                val result = api.postLinkStorebox(map).execute()
                runOnUIThread {
                    output?.storeboxAccountFound(result.isSuccessful)
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onError(exceptionToViewError(t))
            }
        }
    }

}