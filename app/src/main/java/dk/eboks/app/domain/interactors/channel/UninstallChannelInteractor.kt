package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface UninstallChannelInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val id: Int)

    interface Output {
        fun onUninstallChannel()
        fun onUninstallChannelError(error: ViewError)
    }
}