package dk.nodes.arch.domain.interactor

import android.util.Log
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.executor.TestExecutor

abstract class BaseInteractor(protected val executor: Executor) : Interactor {

    override fun run() {
        executor.execute(Runnable {
            try {
                execute()
            } catch (t: Throwable) {
                if (executor is TestExecutor) {
                    throw t
                } else {
                    Log.e(
                        "BaseInteractor",
                        "Uncaught throwable in thread ${Thread.currentThread()?.name}"
                    )
                    Log.e("BaseInteractor", Log.getStackTraceString(t))
                    submitToHockey(t)
                }
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
                val default_handler =
                    exceptionHandlerCls.cast(Thread.getDefaultUncaughtExceptionHandler())
                val method = exceptionHandlerCls.getMethod(
                    "uncaughtException",
                    Thread::class.java,
                    Throwable::class.java
                )
                method.invoke(default_handler, Thread.currentThread(), t)
            } catch (ex: ClassCastException) {
                //e.printStackTrace()
                Log.e("BaseInteractor", "Could not get HockeySDK uncaught exception handler")
            }
        } catch (e: ClassNotFoundException) {
            Log.e("BaseInteractor", "Could not load HockeyApp SDK Classes, cannot record crash")
        }
    }
}