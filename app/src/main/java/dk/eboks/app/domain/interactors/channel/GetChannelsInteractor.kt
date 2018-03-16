package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetChannelsInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(val cached: Boolean)

    interface Output {
        fun onGetChannels(folders : List<Channel>)
        fun onGetChannelsError(msg : String)
    }
}