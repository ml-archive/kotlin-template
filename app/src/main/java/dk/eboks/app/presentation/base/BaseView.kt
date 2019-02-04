package dk.eboks.app.presentation.base

import dk.eboks.app.domain.models.local.ViewError

/**
 * Created by bison on 22/03/2018.
 */
interface BaseView {
    fun showErrorDialog(error: ViewError)
    fun showToast(msg: String, showLongTime: Boolean = false)
}