package dk.eboks.app.presentation.ui.mail.folder

import dk.eboks.app.domain.interactors.GetFoldersInteractor
import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderPresenter @Inject constructor(val getFoldersInteractor: GetFoldersInteractor) :
        FolderContract.Presenter,
        BasePresenterImpl<FolderContract.View>(),
        GetFoldersInteractor.Output
{
    init {
        getFoldersInteractor.output = this
        getFoldersInteractor.run()
    }

    override fun refresh() {
    }

    override fun onGetFolders(folders: List<Folder>) {
        Timber.e("user folders $folders")
        runAction { v-> v.showUserFolders(folders) }

    }

    override fun onGetSystemFolders(folders: List<Folder>) {
        //Timber.e("system folders $folders")
        runAction { v-> v.showSystemFolders(folders) }
    }

    override fun onGetFoldersError(msg: String) {

    }
}