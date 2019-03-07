package dk.eboks.app.domain.senders.interactors.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.protocol.AliasBody
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by chnt on 21-03-2018.
 * @author chnt
 * @since 21-03-2018.
 */
internal class RegisterInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), RegisterInteractor {

    override var inputSender: RegisterInteractor.InputSender? = null
    override var inputSenderGroup: RegisterInteractor.InputSenderGroup? = null
    override var inputSegment: RegisterInteractor.InputSegment? = null

    override var output: RegisterInteractor.Output? = null

    override fun execute() {
        inputSender?.let {
            Timber.d("Sender")
            try {
                api.registerSender(it.senderId).execute()
                runOnUIThread {
                    output?.onSuccess()
                }
            } catch (t: Throwable) {
                runOnUIThread {
                    output?.onError(exceptionToViewError(t))
                }
            }
        }
        inputSenderGroup?.let {
            Timber.d("SenderGroup")
            try {
                api.registerSenderGroup(
                    it.senderId,
                    it.senderGroup.id,
                    AliasBody(it.senderGroup.alias)
                ).execute()
                runOnUIThread {
                    output?.onSuccess()
                }
            } catch (t: Throwable) {
                runOnUIThread {
                    output?.onError(exceptionToViewError(t))
                }
            }
        }
        inputSegment?.let {
            Timber.d("Segment")
            try {
                api.registerSegment(it.segmentId).execute()
                runOnUIThread {
                    output?.onSuccess()
                }
            } catch (t: Throwable) {
                runOnUIThread {
                    output?.onError(exceptionToViewError(t))
                }
            }
        }

        if (inputSender == null && inputSenderGroup == null && inputSegment == null) {
            runOnUIThread {
                output?.onError(ViewError(null, "Missing input"))
            }
        }
    }
}