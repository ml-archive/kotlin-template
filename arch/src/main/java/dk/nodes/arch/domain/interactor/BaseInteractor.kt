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
            }
        })
    }

    fun runOnUIThread(code: () -> Unit) {
        executor.runOnUIThread(code)
    }

    abstract fun execute()

    private fun submitToHockey(t: Throwable) {
        try {
            val exceptionHandlerCls = Class.forName("net.hockeyapp.android.ExceptionHandler")
            try {
                val defaultHandler =
                    exceptionHandlerCls.cast(Thread.getDefaultUncaughtExceptionHandler())
                val method = exceptionHandlerCls.getMethod(
                    "uncaughtException",
                    Thread::class.java,
                    Throwable::class.java
                )
                method.invoke(defaultHandler, Thread.currentThread(), t)
            } catch (ex: ClassCastException) {
                // e.printStackTrace()
                Timber.e("Could not get HockeySDK uncaught exception handler")
            }
        } catch (e: ClassNotFoundException) {
            Timber.e("Could not load HockeyApp SDK Classes, cannot record crash")
        }
    }
}