package dk.nodes.template.domain.interactors.base

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch

/**
 * Created by bison on 24-06-2017.
 */
/*
    Wraps the client provided execute function in a subroutine and runs it on the common pool
 */
abstract class BaseInteractor : Interactor {
    var job : Job? = null

    // This runs on the CommonPool
    abstract fun execute()

    override fun run() {
        job = launch(CommonPool)
        {
            execute()
        }
    }
}