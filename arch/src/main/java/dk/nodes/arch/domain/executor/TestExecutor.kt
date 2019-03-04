package dk.nodes.arch.domain.executor

/**
 * Singlethreaded executer used for unit testing interactors
 */
class TestExecutor : Executor {

    override fun runOnUIThread(block: () -> Unit) {
        block()
    }

    override fun execute(runnable: Runnable) {
        runnable.run()
    }

    override fun sleepUntilSignalled(condId: String, timeout: Long) {
        return
    }

    override fun signal(condId: String) {}
}