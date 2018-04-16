package dk.eboks.app.presentation.ui.components.profile.drawer

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FingerPrintComponentContract {
    enum class Mode { EMAIL, SOCIAL_SECURITY }
    interface View : BaseView {
        fun setProviderMode(mode: Mode)
    }

    interface Presenter : BasePresenter<View> {
    }
}