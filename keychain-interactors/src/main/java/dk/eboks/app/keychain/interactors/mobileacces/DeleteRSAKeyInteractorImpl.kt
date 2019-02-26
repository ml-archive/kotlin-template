package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class DeleteRSAKeyInteractorImpl @Inject constructor(
    executor: Executor,
    private val cryptoManager: CryptoManager
) : BaseInteractor(executor), DeleteRSAKeyInteractor {
    override var output: DeleteRSAKeyInteractor.Output? = null

    override fun execute() {
        try {
            cryptoManager.deleteAllActivations()
            runOnUIThread {
                output?.onDeleteRSAKeySuccess()
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onDeleteRSAKeyError(exceptionToViewError(t))
            }
        }
    }
}