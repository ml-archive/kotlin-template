package dk.nodes.arch.domain.executor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by bison on 26/07/17.
 */
class KoroutineExecutor : Executor {
    override fun runOnUIThread(block: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            block.invoke()
        }
    }

    override fun execute(block: () -> Unit) {
        GlobalScope.launch(Dispatchers.Default) {
            block.invoke()
        }
    }

    override fun sleepUntilSignalled(condId: String, timeout: Long) {
        SignalDispatcher.sleepUntilSignalled(condId, timeout)
    }

    override fun signal(condId: String) {
        SignalDispatcher.signal(condId)
    }
}