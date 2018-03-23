package dk.eboks.app.presentation.ui.screens.senders.overview

import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SendersOverviewContract {
    interface View : BaseView {
        fun showCollections(collections : List<CollectionContainer>)
    }

    interface Presenter : BasePresenter<View> {
    }
}