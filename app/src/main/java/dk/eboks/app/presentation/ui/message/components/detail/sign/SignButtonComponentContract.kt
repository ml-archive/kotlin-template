package dk.eboks.app.presentation.ui.message.components.detail.sign

import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface SignButtonComponentContract {
    interface View : BaseView {
        fun startSigning(msg : Message)
    }

    interface Presenter : BasePresenter<View> {
        fun sign(msg : Message)
    }
}