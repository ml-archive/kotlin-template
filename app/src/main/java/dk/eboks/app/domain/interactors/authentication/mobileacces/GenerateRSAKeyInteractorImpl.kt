package dk.eboks.app.domain.interactors.authentication.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class GenerateRSAKeyInteractorImpl(executor: Executor, val cryptoManager: CryptoManager) : BaseInteractor(executor), GenerateRSAKeyInteractor {
    override var input: GenerateRSAKeyInteractor.Input? = null
    override var output: GenerateRSAKeyInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                cryptoManager.generateRSAKey(it.userId)
                val deviceActivation = cryptoManager.getActivation(it.userId)
                deviceActivation?.publicKey?.let { publicKey->
                val rsakey = cryptoManager.getPublicKeyAsString(publicKey)
                        runOnUIThread { output?.onGenerateRSAKeySuccess(rsakey) }
            }}
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGenerateRSAKeyError(exceptionToViewError(t))
            }
        }
    }
}