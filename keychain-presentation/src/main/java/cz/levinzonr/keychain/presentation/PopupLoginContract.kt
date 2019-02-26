package cz.levinzonr.keychain.presentation

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface PopupLoginContract {
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}