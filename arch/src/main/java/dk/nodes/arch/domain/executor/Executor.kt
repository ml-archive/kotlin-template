package dk.nodes.arch.domain.executor

/**
 * Created by bison on 26/07/17.
 */
interface Executor {
    fun execute(block: () -> Unit)

    /**
     * Pretty much what it says it does, runs the closure on the ui thread (on android mainlooper)
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