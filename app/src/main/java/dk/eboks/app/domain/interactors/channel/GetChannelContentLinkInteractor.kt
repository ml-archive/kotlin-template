package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetChannelContentLinkInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(val channelId : Int)

    interface Output {
        fun onGetChannelContentLink(link : Link)
        fun onGetChannelContentLinkError(error : ViewError)
    }
}