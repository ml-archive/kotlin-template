package dk.nodes.arch.domain.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by bison on 26/07/17.
 */
class ThreadExecutor : Executor {
    override fun runOnUIThread(block: () -> Unit) {
        Handler(Looper.getMainLooper()).post({
            block()
        })
    }

    val CORE_POOL_SIZE = 3
    val MAX_POOL_SIZE = 5
    val KEEP_ALIVE_TIME = 120
    val TIME_UNIT = TimeUnit.SECONDS
    val WORK_QUEUE = LinkedBlockingQueue<Runnable>()

    private var threadPoolExecutor: ThreadPoolExecutor

    init {
        val keepAlive: Long = KEEP_ALIVE_TIME.toLong()
        threadPoolExecutor = ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                keepAlive,
                TIME_UNIT,
                WORK_QUEUE)
    }

    override fun execute(block: () -> Unit) {
        threadPoolExecutor.submit(block)
    }

    override fun sleepUntilSignalled(condId: String, timeout: Long) {
        SignalDispatcher.sleepUntilSignalled(condId, timeout)
    }

    override fun signal(condId: String) {
        SignalDispatcher.signal(condId)
    }
}