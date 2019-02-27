package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class DeleteRSAKeyForUserInteractorImpl @Inject constructor(
    executor: Executor,
    private val cryptoManager: CryptoManager
) :
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