package dk.eboks.app.presentation.ui.screens.message.reply

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class ReplyFormPresenter(val appStateManager: AppStateManager) : ReplyFormContract.Presenter, BasePresenterImpl<ReplyFormContract.View>() {
    init {
    }

    override fun setup(msg: Message) {
        Timber.e("Setting up reply form for message $msg")
    }
}