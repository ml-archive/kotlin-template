package dk.eboks.app.domain.interactors.sender.register

import dk.eboks.app.domain.models.sender.SenderGroup
import dk.nodes.arch.domain.interactor.Interactor

/**
* Created by chnt on 21-03-2017.
* @author   chnt
* @since    21-03-2017.
*/
interface RegisterSenderGroupInteractor : Interactor
{
    var input : Input?
    var output : Output?

    data class Input(val senderId: Long, val senderGroup: SenderGroup)

    interface Output {
        fun onSuccess()
        fun onError(msg : String)
    }
}