package dk.eboks.app.profile.presentation.ui.screens

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MyInfoContract {
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}