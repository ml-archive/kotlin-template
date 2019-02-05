package dk.eboks.app.domain.interactors.authentication.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteRSAKeyForUserInteractorImpl(executor: Executor, val cryptoManager: CryptoManager) :
    BaseInteractor(executor), DeleteRSAKeyForUserInteractor {
    override var input: DeleteRSAKeyForUserInteractor.Input? = null
    override var output: DeleteRSAKeyForUserInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                cryptoManager.deleteActivation(it.userId)
                runOnUIThread {
                    output?.onDeleteRSAKeyForUserSuccess()
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onDeleteRSAKeyForUserError(exceptionToViewError(t))
            }
        }
    }
}