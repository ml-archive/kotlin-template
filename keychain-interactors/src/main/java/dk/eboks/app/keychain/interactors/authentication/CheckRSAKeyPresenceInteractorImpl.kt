package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by Christian on 5/29/2018.
 * @author Christian
 * @since 5/29/2018.
 */
class CheckRSAKeyPresenceInteractorImpl @Inject constructor(
    executor: Executor,
    private val cryptoManager: CryptoManager
) : BaseInteractor(executor), CheckRSAKeyPresenceInteractor {
    override var input: CheckRSAKeyPresenceInteractor.Input? = null
    override var output: CheckRSAKeyPresenceInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                // do not do output?.onCheckRSAKeyPresence(cryptoManager.hasActivation(it.userId)
                // because you think you're smart.. This will cause the checking logic to run in the
                // UI thread for no reason, other than you saving a few key strokes
                if (cryptoManager.hasActivation(it.userId)) {
                    runOnUIThread { output?.onCheckRSAKeyPresence(true) }
                } else
                // todo change this to false once they get the api to work...
                // mobileaccess login
                    runOnUIThread { output?.onCheckRSAKeyPresence(false) }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onCheckRSAKeyPresenceError(exceptionToViewError(t))
            }
        }
    }
}