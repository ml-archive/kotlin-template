package dk.eboks.app.presentation.ui.home.screens

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface HomeContract {
    interface View : BaseView {
        fun addFolderPreview(folder: Folder)
        fun addChannelControl()
        fun showMailsHeader(show: Boolean)
        fun showChannelControlsHeader(show: Boolean)
        fun showFolderProgress(show: Boolean)
        fun showFolder(messages: List<Message>, verifiedUser: Boolean)
        fun showChannelProgress(show: Boolean)
        fun setupChannels(channels: MutableList<Channel>)
        fun updateControl(channel : Channel, control : Control)
        fun setControl(channel : Channel, text : String)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun refresh()
        var continueFetching: Boolean
    }
}