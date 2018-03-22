package dk.eboks.app.domain.interactors.sender.register

import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by chnt on 21-03-2018.
 * @author   chnt
 * @since    21-03-2018.
 */
class RegisterSenderGroupInteractorImpl(executor: Executor, val api: Api) : BaseInteractor(executor), RegisterSenderGroupInteractor {
    override var output: RegisterSenderGroupInteractor.Output? = null
    override var input: RegisterSenderGroupInteractor.Input? = null

    override fun execute() {
        try {
            input?.let {
                api.registerSenderGroup(it.senderId, it.senderGroup.id, it.senderGroup.alias)

                runOnUIThread {
                    output?.onSuccess()
                }
            }
            if (input == null) {
                runOnUIThread {
                    output?.onError("Interactor missing input")
                }
            }

        } catch (e: Exception) {
            runOnUIThread {
                output?.onError(e.message ?: "Unknown error")
            }
        }
    }
}