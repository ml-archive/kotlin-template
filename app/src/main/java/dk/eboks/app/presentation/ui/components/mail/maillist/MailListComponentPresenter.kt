package dk.eboks.app.presentation.ui.components.mail.maillist

import dk.eboks.app.domain.interactors.GetMessagesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailListComponentPresenter @Inject constructor(val appState: AppStateManager, val getMessageInteractor : GetMessagesInteractor) : MailListComponentContract.Presenter, BasePresenterImpl<MailListComponentContract.View>() {

    init {
    }

}