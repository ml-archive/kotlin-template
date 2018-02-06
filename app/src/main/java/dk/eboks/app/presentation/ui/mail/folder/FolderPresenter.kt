package dk.eboks.app.presentation.ui.mail.folder

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderPresenter @Inject constructor() :
        FolderContract.Presenter,
        BasePresenterImpl<FolderContract.View>()
{


    init {
    }

    override fun refresh() {
    }

}