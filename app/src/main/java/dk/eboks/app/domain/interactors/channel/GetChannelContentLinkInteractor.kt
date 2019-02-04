package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetChannelContentLinkInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val channelId: Int)

    interface Output {
        fun onGetChannelContentLink(content: String)
        fun onGetChannelContentLinkError(error: ViewError)
    }
}