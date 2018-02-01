package dk.eboks.app.presentation.ui.mail

import dk.eboks.app.domain.interactors.GetSendersInteractor
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailOverviewPresenter @Inject constructor(val getSendersInteractor: GetSendersInteractor) :
        MailOverviewContract.Presenter,
        BasePresenterImpl<MailOverviewContract.View>(),
        GetSendersInteractor.Output
{
    init {
        getSendersInteractor.output = this
        getSendersInteractor.run()
    }

    override fun onGetSenders(senders: List<Sender>) {
        Timber.e("Received them senders")
        runAction { v ->
            v.showSenders(senders)
        }
    }

    override fun onGetSendersError(msg: String) {
        Timber.e(msg)
    }
}