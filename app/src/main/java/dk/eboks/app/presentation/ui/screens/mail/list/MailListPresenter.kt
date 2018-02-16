package dk.eboks.app.presentation.ui.screens.mail.list

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailListPresenter @Inject constructor(val appState: AppStateManager) :
        MailListContract.Presenter,
        BasePresenterImpl<MailListContract.View>()
{
    init {
        appState.state?.currentFolder?.let {
            runAction { v-> v.showFolderName(it.name) }
        }
    }

}