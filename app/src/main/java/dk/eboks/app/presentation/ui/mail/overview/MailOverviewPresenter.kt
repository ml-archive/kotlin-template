package dk.eboks.app.presentation.ui.mail.overview

import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.GetSendersInteractor
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailOverviewPresenter @Inject constructor(val getSendersInteractor: GetSendersInteractor, val getCategoriesInteractor: GetCategoriesInteractor) :
        MailOverviewContract.Presenter,
        BasePresenterImpl<MailOverviewContract.View>(),
        GetSendersInteractor.Output,
        GetCategoriesInteractor.Output
{
    var refreshingSenders = false
    var refreshingFolders = false

    init {
        getSendersInteractor.input = GetSendersInteractor.Input(true)
        getSendersInteractor.output = this
        getSendersInteractor.run()

        getCategoriesInteractor.input = GetCategoriesInteractor.Input(true)
        getCategoriesInteractor.output = this
        getCategoriesInteractor.run()
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
        getCategoriesInteractor.input = GetCategoriesInteractor.Input(false)
        getCategoriesInteractor.run()
        refreshingFolders = true
        refreshingSenders = true
    }

    override fun onGetSenders(senders: List<Sender>) {
        Timber.e("Received them senders")
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

    override fun onGetCategories(folders: List<Folder>) {
        Timber.e("Received them folders")
        refreshingFolders = false
        runAction { v ->
            v.showFolders(folders)
            updateRefreshProgress()
        }
    }

    override fun onGetCategoriesError(msg: String) {
        refreshingFolders = false
        runAction { v-> v.showRefreshProgress(false) }
        Timber.e(msg)
    }
}