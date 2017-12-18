package dk.eboks.app.domain.interactors

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
        fun onBootstrapDone()
        fun onBootstrapError(msg : String)
    }
}