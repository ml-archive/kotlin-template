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
        internal set

    init {
        getChannelContentLinkInteractor.output = this
    }

    override fun setup(channel: Channel) {
        currentChannel = channel
        view { showChannel(channel) }
        getChannelContentLinkInteractor.input =
            GetChannelContentLinkInteractor.Input(channelId = channel.id)
        getChannelContentLinkInteractor.run()
    }

    override fun onGetChannelContentLink(content: String) {
        view { openChannelContent(content) }
    }

    override fun onGetChannelContentLinkError(error: ViewError) {
        view { showErrorDialog(error) }
    }
}