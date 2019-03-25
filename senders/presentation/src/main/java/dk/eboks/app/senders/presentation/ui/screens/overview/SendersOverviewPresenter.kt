package dk.eboks.app.senders.presentation.ui.screens.overview

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.senders.interactors.GetCollectionsInteractor
import dk.eboks.app.domain.senders.interactors.GetSenderCategoriesInteractor
import dk.eboks.app.domain.senders.interactors.register.GetPendingInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class SendersOverviewPresenter @Inject constructor(
    collectionsInteractor: GetCollectionsInteractor,
    getPendingInteractor: GetPendingInteractor,
    private val registerInteractor: RegisterInteractor,
    private val unRegisterInteractor: UnRegisterInteractor,
    getSenderCategoriesInteractor: GetSenderCategoriesInteractor
) :
    SendersOverviewContract.Presenter,
    BasePresenterImpl<SendersOverviewContract.View>(),
    GetCollectionsInteractor.Output,
    RegisterInteractor.Output,
    UnRegisterInteractor.Output, GetPendingInteractor.Output, GetSenderCategoriesInteractor.Output {

    init {
        collectionsInteractor.output = this
        registerInteractor.output = this
        unRegisterInteractor.output = this
        getPendingInteractor.output = this
        getSenderCategoriesInteractor.output = this
        getSenderCategoriesInteractor.input = GetSenderCategoriesInteractor.Input(true)
        collectionsInteractor.input = GetCollectionsInteractor.Input(false)
        launchOnIO {
            collectionsInteractor.run()
            getPendingInteractor.run()
            getSenderCategoriesInteractor.run()
        }
    }

    override fun onGetCollections(collections: List<CollectionContainer>) {
        Timber.i("Collection loaded")
        launchOnIO {
            collections.forEach {
                Timber.d("Container type: ${it.type}")
            }
        }
        view{
            Timber.d("print: ${collections.first()}")
            showCollections(collections)
        }
    }

    override fun onRegistrationsLoaded(registrations: List<CollectionContainer>) {
        view {
            if (registrations.isEmpty()) {
                hidePendingRegistrations()
            } else {
                showPendingRegistrations(registrations)
            }
        }
    }

    override fun unregisterSender(senderId: Long) {
        unRegisterInteractor.inputSender = UnRegisterInteractor.InputSender(senderId)
        unRegisterInteractor.run()
    }

    override fun registerSender(senderId: Long) {
        registerInteractor.inputSender = RegisterInteractor.InputSender(senderId)
        registerInteractor.run()
    }

    override fun onSuccess() {
        Timber.i("Success")
        view { showSuccess() }
    }

    override fun onGetCategories(categories: List<SenderCategory>) {
        view { showCategories(categories) }
    }

    override fun onGetCategoriesError(error: ViewError) {
        view { showError(error.message ?: "") }
    }

    override fun onError(error: ViewError) {
        view { showError(error.message ?: "") }
    }
}