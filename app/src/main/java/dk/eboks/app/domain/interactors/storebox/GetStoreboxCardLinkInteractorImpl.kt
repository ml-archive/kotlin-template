package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class GetStoreboxCardLinkInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        GetStoreboxCardLinkInteractor {
    override var output: GetStoreboxCardLinkInteractor.Output? = null

    override fun execute() {
        try {
            val result = api.getStoreboxCardLink().execute()

            result.body()?.let {
                runOnUIThread {
                    output?.onGetStoreboxCardLink(it)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()

            runOnUIThread {
                output?.onGetStoreboxCardLinkError(exceptionToViewError(t))
            }
        }
    }
}