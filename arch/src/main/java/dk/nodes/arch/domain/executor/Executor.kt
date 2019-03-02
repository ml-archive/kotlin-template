package dk.nodes.arch.domain.executor

interface Executor {

    fun execute(runnable: Runnable)

    /**
     * Runs the lambda on the UI thread (On android main thread)
     */
    fun runOnUIThread(block: () -> Unit)

    /**
     * Blocks the calling interactor thread until it its notified by another thread
     * This is used for stuff such as launching a permission request from an
     * interactor and suspending executing until the UI flow has either succeded or failed
     */
    fun sleepUntilSignalled(condId: String, timeout: Long = 0)

    fun signal(condId: String)
}