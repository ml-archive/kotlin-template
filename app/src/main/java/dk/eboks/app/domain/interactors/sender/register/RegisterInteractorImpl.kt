package dk.eboks.app.domain.interactors.sender.register

import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by chnt on 21-03-2018.
 * @author   chnt
 * @since    21-03-2018.
 */
class RegisterInteractorImpl(executor: Executor, val api: Api) : BaseInteractor(executor), RegisterInteractor {

    override var inputSender: RegisterInteractor.InputSender? = null
    override var inputSenderGroup: RegisterInteractor.InputSenderGroup? = null
    override var inputSegment: RegisterInteractor.InputSegment? = null

    override var output: RegisterInteractor.Output? = null

    override fun execute() {
        inputSender?.let {
            try {
                api.registerSender(it.senderId)
                runOnUIThread {
                    output?.onSuccess()
                }
            } catch (e: Exception) {
                runOnUIThread {
                    output?.onError(e.message ?: "Unknown error")
                }
            }
        }
        inputSenderGroup?.let {
            try {
                api.registerSenderGroup(it.senderId, it.senderGroup.id, it.senderGroup.alias)
                runOnUIThread {
                    output?.onSuccess()
                }
            } catch (e: Exception) {
                runOnUIThread {
                    output?.onError(e.message ?: "Unknown error")
                }
            }
        }
        inputSegment?.let {
            try {
                api.registerSegment(it.segmentId)
                runOnUIThread {
                    output?.onSuccess()
                }
            } catch (e: Exception) {
                runOnUIThread {
                    output?.onError(e.message ?: "Unknown error")
                }
            }
        }

        if (inputSender == null && inputSenderGroup == null && inputSegment == null) {
            runOnUIThread {
                output?.onError("Missing input")
            }
        }
    }
}