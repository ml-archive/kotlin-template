package dk.eboks.app.presentation.ui.channels.components.content.web

import dk.eboks.app.domain.interactors.channel.GetChannelContentLinkInteractor
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */

internal class ChannelContentComponentPresenter @Inject constructor(
    private val getChannelContentLinkInteractor: GetChannelContentLinkInteractor
) :
    ChannelContentComponentContract.Presenter,
    BasePresenterImpl<ChannelContentComponentContract.View>(),
    GetChannelContentLinkInteractor.Output {
    override var currentChannel: Channel? = null

    init {
        getChannelContentLinkInteractor.output = this
    }

    override fun setup(channel: Channel) {
        currentChannel = channel
        runAction { v -> v.showChannel(channel) }
        getChannelContentLinkInteractor.input =
            GetChannelContentLinkInteractor.Input(channelId = channel.id)
        getChannelContentLinkInteractor.run()
    }

    override fun onGetChannelContentLink(content: String) {
        runAction { v -> v.openChannelContent(content) }
    }

    override fun onGetChannelContentLinkError(error: ViewError) {
        runAction { v -> v.showErrorDialog(error) }
    }
}