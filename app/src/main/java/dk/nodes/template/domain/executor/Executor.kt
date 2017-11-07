package dk.nodes.template.domain.executor

/**
 * Created by bison on 26/07/17.
 */
interface Executor {
    fun execute(runnable: Runnable)
    fun runOnUIThread(code : ()->Unit)
}