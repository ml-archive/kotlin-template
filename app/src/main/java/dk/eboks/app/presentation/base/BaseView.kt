package dk.eboks.app.presentation.base

import dk.eboks.app.domain.models.protocol.ServerError

/**
 * Created by bison on 22/03/2018.
 */
interface BaseView {
    fun showServerError(serverError: ServerError)
}