package dk.eboks.app.pasta.activity

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface PastaContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}