package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface TogglePaymentNotificationInteractor : Interactor {

    var input: Input?
    var output: Output?

    data class Input(val folderId: Int, val messageId: String, val on: Boolean)

    interface Output {
        fun onNotificationsToggleUpdated(newValue: Boolean)
        fun onNotificationToggleUpdateError(viewError: ViewError)
    }
}