package dk.eboks.app.domain.senders.interactors.register

import dk.eboks.app.domain.models.local.ViewError
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
internal class UnRegisterInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api
) : BaseInteractor(executor), UnRegisterInteractor {

    override var inputSender: UnRegisterInteractor.InputSender? = null
    override var inputSenderGroup: UnRegisterInteractor.InputSenderGroup? = null
    override var inputSegment: UnRegisterInteractor.InputSegment? = null

    override var output: UnRegisterInteractor.Output? = null

    override fun execute() {
        inputSender?.let {
            Timber.d("Sender")
            try {
                api.unregisterSender(it.senderId).execute()
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
                api.unregisterSenderGroup(it.senderId, it.senderGroup.id).execute()
//                api.unregisterSenderGroup(it.senderId, it.senderGroup.id, AliasBody(it.senderGroup.alias)).execute() // TODO: we probably need the alias to a url param instead
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
                api.unregisterSegment(it.segmentId).execute()
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