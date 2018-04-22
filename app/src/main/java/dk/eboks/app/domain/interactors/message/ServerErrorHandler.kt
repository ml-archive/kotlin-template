package dk.eboks.app.domain.interactors.message

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
        val MANDATORY_OPEN_RECEIPT = 9200
        val VOLUNTARY_OPEN_RECEIPT = 9201
        val MESSAGE_QUARANTINED = 9300
        val MESSAGE_RECALLED = 9301
        val MESSAGE_LOCKED = 9302
        val PROMULGATION = 9400

        val PROCEED = 1
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
        // this is probably slow as we allocate a collection just to do a check :)
        if(setOf(
                NO_PRIVATE_SENDER_WARNING,
                MANDATORY_OPEN_RECEIPT,
                VOLUNTARY_OPEN_RECEIPT,
                MESSAGE_QUARANTINED,
                MESSAGE_RECALLED,
                MESSAGE_LOCKED,
                PROMULGATION).contains(error.code))
        {
            appStateManager.state?.let { state->
                state.openingState.shouldProceedWithOpening = false
                state.openingState.serverError = error
            }

            //uiManager.showMessageOpeningScreen()
            executor.sleepUntilSignalled("messageOpenDone")
            if(appStateManager.state?.openingState?.shouldProceedWithOpening ?: false)
                return PROCEED
            else
                return ABORT
        }
        else
            return SHOW_ERROR
    }
}