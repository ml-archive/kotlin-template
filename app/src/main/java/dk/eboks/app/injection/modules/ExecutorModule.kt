package dk.eboks.app.injection.modules

import android.os.ConditionVariable
import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by bison on 26/07/17.
 */
@Module
class ExecutorModule {
    @Provides
    @AppScope
    fun provideExecutor(): Executor {
        return MyThreadExecutor()
    }
}

class MyThreadExecutor : Executor {
    override fun runOnUIThread(block: () -> Unit) {
        Handler(Looper.getMainLooper()).post {
            block()
        }
    }

    private val CORE_POOL_SIZE = 3
    private val MAX_POOL_SIZE = 5
    private val KEEP_ALIVE_TIME = 120
    private val TIME_UNIT = TimeUnit.SECONDS
    private val WORK_QUEUE = LinkedBlockingQueue<Runnable>()

    private var threadPoolExecutor: ThreadPoolExecutor

    init {
        val keepAlive: Long = KEEP_ALIVE_TIME.toLong()
        threadPoolExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            keepAlive,
            TIME_UNIT,
            WORK_QUEUE
        )
    }

    override fun execute(runnable: Runnable) {
        threadPoolExecutor.submit(runnable)
    }

    override fun sleepUntilSignalled(condId: String, timeout: Long) {
        MySignalDispatcher.sleepUntilSignalled(condId, timeout)
    }

    override fun signal(condId: String) {
        MySignalDispatcher.signal(condId)
    }
}

object MySignalDispatcher {
    private val activeSignalMap: MutableMap<String, ConditionVariable> = HashMap()

    fun sleepUntilSignalled(condId: String, timeout: Long = 0) {
        // create a condition variable in the default closed state
        val condvar = ConditionVariable()

        // add it along with the identifier to a map
        // if another thread is already waiting for a same signal - release it
        if (activeSignalMap.containsKey(condId)) {
            activeSignalMap[condId]?.open()
        }
        activeSignalMap[condId] = condvar
        // wait forever on that sucker
        if (timeout == 0L)
            condvar.block()
        else // or wake if a certain time has gone by and we still haven't been signalled
            condvar.block(timeout)
    }

    fun signal(condId: String) {
        // signal the waiting thread to wake up
        activeSignalMap[condId]?.open()
        if (activeSignalMap.containsKey(condId)) // to lazy to check if the next statement can throw an exception (its midnight on wednesday [7])
            activeSignalMap.remove(condId)
    }
}