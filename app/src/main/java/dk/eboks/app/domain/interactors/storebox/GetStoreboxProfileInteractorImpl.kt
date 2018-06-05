package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class GetStoreboxProfileInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        GetStoreboxProfileInteractor {
    override var output: GetStoreboxProfileInteractor.Output? = null

    override fun execute() {
        try {
            val result = api.getStoreboxProfile().execute()

            result.body()?.let {
                runOnUIThread {
                    output?.onGetProfile(it)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()

            runOnUIThread {
                output?.onGetProfileError(exceptionToViewError(t))
            }
        }
    }
}