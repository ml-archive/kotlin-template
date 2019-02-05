package dk.eboks.app.domain.interactors.sender.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by chnt on 21-03-2017.
 * @author chnt
 * @since 21-03-2017.
 */
interface UnRegisterInteractor : Interactor {
    var inputSenderGroup: InputSenderGroup?
    var inputSender: InputSender?
    var inputSegment: InputSegment?
    var output: Output?

    data class InputSenderGroup(val senderId: Long, val senderGroup: SenderGroup)
    data class InputSender(val senderId: Long)
    data class InputSegment(val segmentId: Long)

    interface Output {
        fun onSuccess()
        fun onError(error: ViewError)
    }
}