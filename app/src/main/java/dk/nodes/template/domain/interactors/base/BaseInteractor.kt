package dk.nodes.template.domain.interactors.base

import dk.nodes.template.domain.executor.Executor

/**
 * Created by bison on 24-06-2017.
 */
/*
    Wraps the client provided execute function in a subroutine and runs it on the common pool
 */
abstract class BaseInteractor(protected val executor: Executor) : Interactor {
    override fun run() {
        executor.execute(Runnable {
            execute()
        })
    }

    fun runOnUIThread(code: () -> Unit)
    {
        executor.runOnUIThread(code)
    }

    abstract fun execute()
}