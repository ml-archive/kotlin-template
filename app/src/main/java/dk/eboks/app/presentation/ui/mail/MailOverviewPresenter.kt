package dk.eboks.app.presentation.ui.mail

import dk.eboks.app.domain.interactors.GetFoldersInteractor
import dk.eboks.app.domain.interactors.GetSendersInteractor
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailOverviewPresenter @Inject constructor(val getSendersInteractor: GetSendersInteractor, val getFoldersInteractor: GetFoldersInteractor) :
        MailOverviewContract.Presenter,
        BasePresenterImpl<MailOverviewContract.View>(),
        GetSendersInteractor.Output,
        GetFoldersInteractor.Output
{
    var refreshingSenders = false
    var refreshingFolders = false

    init {
        getSendersInteractor.input = GetSendersInteractor.Input(true)
        getSendersInteractor.output = this
        getSendersInteractor.run()

        getFoldersInteractor.input = GetFoldersInteractor.Input(true)
        getFoldersInteractor.output = this
        getFoldersInteractor.run()
    }


    fun updateRefreshProgress()
    {
        if(!refreshingFolders && !refreshingSenders)
        {
            view?.showRefreshProgress(false)
        }
    }

    override fun refresh() {
        getSendersInteractor.input = GetSendersInteractor.Input(false)
        getSendersInteractor.run()
        getFoldersInteractor.input = GetFoldersInteractor.Input(false)
        getFoldersInteractor.run()
        refreshingFolders = true
        refreshingSenders = true
    }

    override fun onGetSenders(senders: List<Sender>) {
        Timber.e("Received them messages")
        refreshingSenders = false
        runAction { v ->
            v.showSenders(senders)
            updateRefreshProgress()
        }
    }

    override fun onGetSendersError(msg: String) {
        refreshingSenders = false
        Timber.e(msg)
        runAction { v-> v.showRefreshProgress(false) }
    }

    override fun onGetFolders(folders: List<Folder>) {
        Timber.e("Received them folders")
        refreshingFolders = false
        runAction { v ->
            v.showFolders(folders)
            updateRefreshProgress()
        }
    }

    override fun onGetFoldersError(msg: String) {
        refreshingFolders = false
        runAction { v-> v.showRefreshProgress(false) }
        Timber.e(msg)
    }
}