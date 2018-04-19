package dk.eboks.app.presentation.ui.screens.message.reply

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class ReplyFormPresenter(val appStateManager: AppStateManager) : ReplyFormContract.Presenter, BasePresenterImpl<ReplyFormContract.View>() {
    init {
    }

}