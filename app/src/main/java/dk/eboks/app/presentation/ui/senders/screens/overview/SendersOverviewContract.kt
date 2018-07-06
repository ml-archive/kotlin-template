package dk.eboks.app.presentation.ui.senders.screens.overview

import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

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