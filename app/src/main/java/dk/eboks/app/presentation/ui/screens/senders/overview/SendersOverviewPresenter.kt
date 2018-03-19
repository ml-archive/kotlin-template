package dk.eboks.app.presentation.ui.screens.senders.overview

import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class SendersOverviewPresenter(val appStateManager: AppStateManager, val collectionsInteractor: GetCollectionsInteractor) : SendersOverviewContract.Presenter, BasePresenterImpl<SendersOverviewContract.View>(),
        GetCollectionsInteractor.Output {

    init {
        collectionsInteractor.output = this
        collectionsInteractor.input = GetCollectionsInteractor.Input(false)
        collectionsInteractor.run()
    }

    override fun onGetCollections(collections: List<CollectionContainer>) {

    }

}