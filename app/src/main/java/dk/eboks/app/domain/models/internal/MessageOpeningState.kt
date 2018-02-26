package dk.eboks.app.domain.models.internal

import dk.eboks.app.domain.models.ServerError

/**
 * Created by bison on 09-02-2018.
 */
data class MessageOpeningState(
        var shouldProceedWithOpening : Boolean = false,
        var serverError: ServerError? = null
)
