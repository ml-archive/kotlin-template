package dk.eboks.app.presentation.ui.senders.components

import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by Christian on 3/15/2018.
 * @author Christian
 * @since 3/15/2018.
 */
class SenderGroupsComponentPresenter @Inject constructor() :
    SenderGroupsComponentContract.Presenter,
    BasePresenterImpl<SenderGroupsComponentContract.View>() {

    init {
    }

    override fun getSenderGroups(sender: Sender) {
        runAction { v ->
            sender.groups?.let {
                v.showSenderGroups(sender)
            }
        }
    }
}