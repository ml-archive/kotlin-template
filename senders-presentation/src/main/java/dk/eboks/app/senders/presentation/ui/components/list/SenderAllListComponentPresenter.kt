package dk.eboks.app.senders.presentation.ui.components.list

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class SenderAllListComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val getSendersInteractor: GetSendersInteractor
) : SenderAllListComponentContract.Presenter,
    BasePresenterImpl<SenderAllListComponentContract.View>(), GetSendersInteractor.Output {

    var senders: MutableList<Sender> = ArrayList()
    private var filteredSenders: MutableList<Sender> = ArrayList()

    init {
        refresh()
        view { showProgress(true) }
    }

    override fun refresh() {
        getSendersInteractor.input =
            GetSendersInteractor.Input(userId = appState.state?.impersoniateUser?.userId)
        getSendersInteractor.output = this
        getSendersInteractor.run()
    }

    override fun onGetSenders(senders: List<Sender>) {

        saveSenders(senders)

        view {
            showProgress(false)
            if (senders.isNotEmpty()) {
                showEmpty(false)
                showSenders(filteredSenders)
            } else {
                showEmpty(true)
            }
        }
    }

    private fun saveSenders(senders: List<Sender>) {
        this.senders.clear()
        this.filteredSenders.clear()
        this.senders.addAll(senders)
        filteredSenders.addAll(this.senders)
    }

    override fun onGetSendersError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun loadAllSenders() {
        filteredSenders.clear()
        filteredSenders.addAll(senders)
        view { showSenders(filteredSenders) }
    }

    override fun searchSenders(searchText: String) {
        filteredSenders.clear()
        for (sender in senders) {
            if (sender.name.contains(searchText, true)) {
                filteredSenders.add(sender)
            }
        }
        view { showSenders(filteredSenders) }
    }
}
