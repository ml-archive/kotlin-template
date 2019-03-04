package dk.nodes.arch.domain.interactor

import dk.nodes.arch.domain.executor.Executor
import timber.log.Timber

abstract class BaseInteractor(protected val executor: Executor) : Interactor {

    override fun run() {
        executor.execute(Runnable {
            try {
                execute()
            } catch (t: Throwable) {
                Timber.e("Uncaught throwable in thread ${Thread.currentThread().name}")
                Timber.e(t)
                throw t
            }
        })
    }

    fun runOnUIThread(code: () -> Unit) {
        executor.runOnUIThread(code)
    }

    abstract fun execute()
}