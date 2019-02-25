package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class PutStoreboxProfileInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), PutStoreboxProfileInteractor {
    override var output: PutStoreboxProfileInteractor.Output? = null
    override var input: PutStoreboxProfileInteractor.Input? = null

    override fun execute() {
        try {
            input?.profile?.let {
                val result = api.putStoreboxProfile(it).execute()
                if (result.isSuccessful) {
                    runOnUIThread {
                        output?.onPutProfile()
                    }
                }
            }.guard { output?.onPutProfileError(ViewError()) }
        } catch (t: Throwable) {
            t.printStackTrace()

            runOnUIThread {
                output?.onPutProfileError(exceptionToViewError(t))
            }
        }
    }
}