package dk.eboks.app.mail.presentation.ui.message.screens.sign

import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by Christian on 5/23/2018.
 * @author Christian
 * @since 5/23/2018.
 */
interface SignContract {
    interface View : BaseView {
        fun loadUrl(urlString: String)
        fun loadData(data: String)
    }

    interface Presenter : BasePresenter<View> {
        fun setup(msg: Message)
    }
}