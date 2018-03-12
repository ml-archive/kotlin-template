package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.protocol.ServerError
import dk.nodes.arch.domain.executor.Executor
import timber.log.Timber

/**
 * Created by bison on 25-02-2018.
 * This is interactor domain code, no external dependencies please
 */
class ServerErrorHandler(val uiManager: UIManager, val executor: Executor, val appStateManager: AppStateManager) {
    companion object {
        val NO_PRIVATE_SENDER_WARNING = 9100

        val REPEAT = 1
        val ABORT = 2
        val SHOW_ERROR = 3
    }


    /*
        Handles the server error, returns true if the interactor
        should continue with whatever it was doing, otherwise
        it returns false to indicate the interactor should abort
     */
    fun handle(error : ServerError) : Int
    {
        Timber.e("Handling server error $error")
        when(error.code)
        {
            NO_PRIVATE_SENDER_WARNING -> {
                Timber.e("Handling private sender warning")
                appStateManager.state?.let { state->
                    state.openingState.shouldProceedWithOpening = false
                    state.openingState.serverError = error
                }
                uiManager.showMessageOpeningScreen()
                executor.sleepUntilSignalled("messageOpenDone")
                Timber.e("Woke back up for some action")
                if(appStateManager.state?.openingState?.shouldProceedWithOpening ?: false)
                    return REPEAT
                else
                    return ABORT
            }
            else -> return SHOW_ERROR
        }
    }
}