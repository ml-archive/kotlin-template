package dk.eboks.app.presentation.ui.message.components.detail.reply

import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ReplyButtonComponentPresenter @Inject constructor() :
    ReplyButtonComponentContract.Presenter, BasePresenterImpl<ReplyButtonComponentContract.View>() {

    init {
    }

    override fun reply(msg: Message) {
        runAction { v -> v.showReplyForm(msg) }
    }
}