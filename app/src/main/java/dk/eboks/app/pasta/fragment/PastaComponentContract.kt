package dk.eboks.app.pasta.fragment

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface PastaComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}