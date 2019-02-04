package dk.eboks.app.domain.models.message

import dk.eboks.app.domain.models.protocol.ServerError

/**
 * Created by bison on 09-02-2018.
 */
data class MessageOpeningState(
    var shouldProceedWithOpening: Boolean = false,
    var sendReceipt: Boolean = false,
    var acceptPrivateTerms: Boolean = false, // stores whether or not norwegian users logged on via idporten can open messages from private senders
    var serverError: ServerError? = null
)
