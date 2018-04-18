package dk.eboks.app.presentation.ui.components.message.detail.reply

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ReplyButtonComponentPresenter @Inject constructor(val appState: AppStateManager) : ReplyButtonComponentContract.Presenter, BasePresenterImpl<ReplyButtonComponentContract.View>() {

    init {
    }

}