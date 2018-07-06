package dk.eboks.app.presentation.ui.senders.screens.overview

import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class SendersOverviewPresenter(val appStateManager: AppStateManager, val collectionsInteractor: GetCollectionsInteractor) :
        SendersOverviewContract.Presenter,
        BasePresenterImpl<SendersOverviewContract.View>(),
        GetCollectionsInteractor.Output {

    init {
        collectionsInteractor.output = this
        collectionsInteractor.input = GetCollectionsInteractor.Input(true)
        collectionsInteractor.run()
    }

    override fun onGetCollections(collections: List<CollectionContainer>) {

        Timber.i("Collection loaded")
        collections.forEach {
            Timber.d("Container type: ${it.type}")
        }

        runAction { v ->
            v.showCollections(collections)
        }
    }

}