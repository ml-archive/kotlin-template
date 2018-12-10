package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 24-06-2017.
 */
interface BootstrapInteractor : Interactor
{
    var input : Input?
    var output : Output?

    data class Input(val i : Int)

    interface Output {
        fun onBootstrapDone(hasUsers : Boolean, autoLogin : Boolean)
        fun onBootstrapError(error : ViewError)
    }
}