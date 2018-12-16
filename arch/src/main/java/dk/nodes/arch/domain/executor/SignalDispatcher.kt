package dk.nodes.arch.domain.executor

import android.os.ConditionVariable

/**
 * Created by bison on 21-02-2018.
 */
object SignalDispatcher {
    private val activeSignalMap: MutableMap<String, ConditionVariable> = HashMap()

    fun sleepUntilSignalled(condId: String, timeout: Long = 0) {
        // create a condition variable in the default closed state
        val condvar = ConditionVariable()
        // add it along with the identifier to a map
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