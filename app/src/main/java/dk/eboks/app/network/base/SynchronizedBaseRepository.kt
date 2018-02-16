package dk.eboks.app.network.base

import dk.eboks.app.BuildConfig
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by bison on 12/02/18.
 */
abstract class SynchronizedBaseRepository {

    companion object {
        private val pipelineLock = ReentrantLock()

        fun lock()
        {
            if(BuildConfig.FORCE_REQUEST_PIPELINING) {
                //Timber.e("lock")
                pipelineLock.lock()
            }
        }

        fun unlock()
        {
            if(BuildConfig.FORCE_REQUEST_PIPELINING) {
                //Timber.e("unlock")
                if(pipelineLock.isHeldByCurrentThread)
                    pipelineLock.unlock()
            }

        }
    }
}