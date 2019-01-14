package dk.eboks.app.presentation.ui.senders.screens.overview

import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.CollectionContainerTypeEnum
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.typeEnum
import dk.nodes.arch.presentation.base.BasePresenterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
    UnRegisterInteractor.Output {
    init {
        collectionsInteractor.output = this
        registerInteractor.output = this
        unRegisterInteractor.output = this
        collectionsInteractor.input = GetCollectionsInteractor.Input(false)
        collectionsInteractor.run()
    }

    override fun onGetCollections(collections: List<CollectionContainer>) {
        Timber.i("Collection loaded")
        GlobalScope.launch(Dispatchers.Main) {
            val unregisteredCount = async(Dispatchers.IO) {
                var count = 0
                collections.forEach {
                    Timber.d("Container type: ${it.type}")
                    when {
                        it.typeEnum == CollectionContainerTypeEnum.SENDER && it.sender?.registered == 0 -> count++
                        it.typeEnum == CollectionContainerTypeEnum.SEGMENT && it.segment?.registered == 0 -> count++
                        it.typeEnum == CollectionContainerTypeEnum.SENDERS -> it.senders?.forEach { if (it.registered == 0) count++ }
                    }
                }
                return@async count
            }.await()
            runAction { v ->
                v.showCollections(collections)
                if (unregisteredCount > 0) {
                    v.showPendingRegistrations(
                        Translation.senders.pendingRegistrations.replace(
                            "[COUNT]",
                            unregisteredCount.toString()
                        )
                    )
                } else {
                    v.hidePendingRegistrations()
                }

            }
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
            v.showError(error.message ?: "")
        }
    }
}