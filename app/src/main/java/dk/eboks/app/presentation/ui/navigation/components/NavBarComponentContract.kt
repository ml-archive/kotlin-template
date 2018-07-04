package dk.eboks.app.presentation.ui.navigation.components

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface NavBarComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}