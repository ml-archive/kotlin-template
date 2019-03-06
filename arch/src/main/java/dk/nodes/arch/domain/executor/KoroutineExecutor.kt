package dk.nodes.arch.domain.executor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class KoroutineExecutor : Executor {

    override fun runOnUIThread(block: () -> Unit) {
        launchOnUI { block() }
    }

    override fun execute(runnable: Runnable) {
        launch { runnable.run() }
    }

    override fun sleepUntilSignalled(condId: String, timeout: Long) {
        SignalDispatcher.sleepUntilSignalled(condId, timeout)
    }

    override fun signal(condId: String) {
        SignalDispatcher.signal(condId)
    }

    private fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) = GlobalScope.launch(context = context, block = block)

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) =
        launch(context = Dispatchers.Main, block = block)
}