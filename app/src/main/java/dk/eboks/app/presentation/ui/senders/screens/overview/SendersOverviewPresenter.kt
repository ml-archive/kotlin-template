package dk.eboks.app.presentation.ui.senders.screens.overview

import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class SendersOverviewPresenter(
    collectionsInteractor: GetCollectionsInteractor,
    private val registerInteractor: RegisterInteractor,
    private val unRegisterInteractor: UnRegisterInteractor
) :
    SendersOverviewContract.Presenter,
    BasePresenterImpl<SendersOverviewContract.View>(),
    GetCollectionsInteractor.Output,
    RegisterInteractor.Output,
    UnRegisterInteractor.Output{
    init {
        collectionsInteractor.output = this
        registerInteractor.output = this
        unRegisterInteractor.output = this
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

    override fun unregisterSender(sender: Sender) {
        registerInteractor.inputSender = RegisterInteractor.InputSender(sender.id)
        registerInteractor.run()
    }

    override fun registerSender(sender: Sender) {
        unRegisterInteractor.inputSender = UnRegisterInteractor.InputSender(sender.id)
        unRegisterInteractor.run()
    }

    override fun onSuccess() {
        Timber.i("Success")
        runAction { v ->
            v.showSuccess()
        }
    }

    override fun onError(error: ViewError) {
        runAction { v ->
            v.showError(error.message?:"")
        }
    }
}